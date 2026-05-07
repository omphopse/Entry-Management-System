
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.time.LocalDate;

public class BSFEntryManagementSystem {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        List<User> users = new ArrayList<>();

        List<Visitor> visitors = new ArrayList<>();
        List<VisaApplication> applications = new ArrayList<>();
        List<Visa> visas = new ArrayList<>();
        List<BorderEntryRecord> entryRecords = new ArrayList<>();
        List<Notification> notifications = new ArrayList<>();
        List<Warrant> warrants = new ArrayList<>();

        BSFFunctions.loadUsersFromExcel(users);

        BSFFunctions.loadVisitorsFromExcel(visitors, users);
        BSFFunctions.loadVisaApplicationsFromExcel(applications, visitors);
        BSFFunctions.loadVisasFromExcel(visas, visitors, applications);
        BSFFunctions.loadBorderEntryRecordsFromExcel(entryRecords, visitors, visas);

        NotificationService notificationService = new NotificationService(visas, entryRecords, notifications, warrants);
        notificationService.start();

        String name = null;
        String password = null;
        int choice = 0;

        System.out.println("\t\tWelcome to the BSF Entry Management System");

        while (true) {
            System.out.println("\n\n1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");

            System.out.print("Select your choice: ");
            String choiceLine = sc.nextLine().trim();
            try {
                choice = Integer.parseInt(choiceLine);
            } catch (NumberFormatException nfe) {
                System.out.println("Invalid input. Please enter 1, 2 or 3.");
                continue;
            }
            switch (choice) {
                case 1:
                    System.out.print("\nEnter username: ");
                    name = sc.nextLine();

                    System.out.print("Enter password: ");
                    password = sc.nextLine();

                    boolean found = false;

                    for (User user : users) {
                        if (user.getUsername().equals(name) && user.getPassword().equals(password)) {
                            found = true;
                            User loggedInUser = user;

                            switch (loggedInUser.getRole()) {
                                case APPLICANT:
                                    Visitor currentVisitor = null;
                                    for (Visitor v : visitors) {
                                        if (v.getUserAccount().getId() == loggedInUser.getId()) {
                                            currentVisitor = v;
                                            break;
                                        }
                                    }

                                    if (currentVisitor == null) {
                                        System.out.println("\nNo visitor record found for your account. Let's create one.");
                                        System.out.print("\nEnter passport number: ");
                                        String passport = sc.nextLine();
                                        System.out.print("Enter nationality: ");
                                        String nationality = sc.nextLine();
                                        currentVisitor = new Visitor(loggedInUser, passport, nationality);
                                        visitors.add(currentVisitor);
                                        BSFFunctions.writeVisitorDataToExcel(currentVisitor);
                                        System.out.println("\nVisitor record created for " + loggedInUser.getUsername() + ".");
                                    }

                                    int choice2 = 0;

                                    while (choice2 != 7) {
                                        System.out.println("\n\nWelcome, " + loggedInUser.getUsername()+ "! You are an applicant.");
                                        System.out.println("1. Apply for a visa");
                                        System.out.println("2. View your application status");
                                        System.out.println("3. View Visa");
                                        System.out.println("4. View border entry history");
                                        System.out.println("5. Request an entry");
                                        System.out.println("6. Notifications");
                                        System.out.println("7. Logout");

                                        System.out.print("Enter your choice: ");
                                        choice2 = sc.nextInt();
                                        sc.nextLine();
                                        switch (choice2) {
                                            case 1:
                                                System.out.println(
                                                        "\n\nPress \"1\" for TOURIST \n \"2\" for STUDENT \"3\" for WORK \"4\" for BUSINESS");
                                                System.out.println("Select visa type: ");
                                                int vt = sc.nextInt();
                                                sc.nextLine();
                                                VisaType visaType;
                                                switch (vt) {
                                                    case 1:
                                                        visaType = VisaType.TOURIST;
                                                        break;
                                                    case 2:
                                                        visaType = VisaType.STUDENT;
                                                        break;
                                                    case 3:
                                                        visaType = VisaType.WORK;
                                                        break;
                                                    case 4:
                                                        visaType = VisaType.BUSINESS;
                                                        break;
                                                    default:
                                                        visaType = VisaType.TOURIST;
                                                        break;
                                                }
                                                System.out.print("\nEnter reason for application: ");
                                                String reason = sc.nextLine();
                                                VisaApplication app = new VisaApplication(currentVisitor, visaType,
                                                        LocalDate.now(), ApplicationStatus.PENDING, reason, null, null);
                                                applications.add(app);
                                                BSFFunctions.writeVisaApplicationDataToExcel(app);
                                                // Send notification for visa application submitted
                                                NotificationManager.notifyVisaApplicationSubmitted(currentVisitor, app, notifications);
                                                System.out.println(
                                                        "\nApplication submitted. Your application ID: " + app.getId());
                                                break;
                                            case 2:
                                                boolean any = false;
                                                System.out.println("\n========== Your Visa Applications ==========\n");
                                                System.out.printf("%-5s | %-10s | %-15s | %-10s | %-15s | %-10s%n",
                                                        "ID", "Visa Type", "App Date", "Status", "Reason", "Rejection");
                                                System.out.println("-".repeat(90));
                                                for (VisaApplication a : applications) {
                                                    if (a.getApplicant().getId() == currentVisitor.getId()) {
                                                        any = true;
                                                        String reason1 = a.getReason() != null ? a.getReason() : "N/A";
                                                        String rejection = a.getRejectionReason() != null
                                                                ? a.getRejectionReason()
                                                                : "N/A";
                                                        if (reason1.length() > 15)
                                                            reason1 = reason1.substring(0, 12) + "...";
                                                        if (rejection.length() > 10)
                                                            rejection = rejection.substring(0, 7) + "...";
                                                        System.out.printf(
                                                                "%-5d | %-10s | %-15s | %-10s | %-15s | %-10s%n",
                                                                a.getId(), a.getVisaType(), a.getApplicationDate(),
                                                                a.getStatus(), reason1, rejection);
                                                    }
                                                }
                                                System.out.println("-".repeat(90));
                                                if (!any) {
                                                    System.out.println("No applications found for your account.");
                                                }
                                                System.out.println();
                                                break;
                                            case 3:
                                                BSFFunctions.displayVisasForUser(loggedInUser);
                                                break;
                                            case 4:
                                                BSFFunctions.displayBorderHistoryForApplicant(loggedInUser,
                                                        entryRecords);
                                                break;
                                            case 5:
                                                List<Visa> myVisas = new ArrayList<>();
                                                for (Visa v : visas) {
                                                    if (v.getHolder().getId() == currentVisitor.getId()
                                                            && v.getStatus() == VisaStatus.ACTIVE) {
                                                        myVisas.add(v);
                                                    }
                                                }

                                                if (myVisas.isEmpty()) {
                                                    System.out.println(
                                                            "You do not have any active approved visas. Please apply for a visa first.");
                                                    break;
                                                }

                                                System.out.println("\nYour active approved visas:");
                                                for (Visa v : myVisas) {
                                                    System.out.println(
                                                            "Visa ID: " + v.getId() + ", Visa Number: "
                                                                    + v.getVisaNumber()
                                                                    + ", Type: " + v.getVisaType() + ", Expiry: "
                                                                    + v.getExpiryDate() + ", Status: " + v.getStatus());
                                                }

                                                System.out.print("Enter the Visa ID you want to use for entry: ");
                                                long visaIdForEntry = sc.nextLong();
                                                sc.nextLine();

                                                Visa selectedVisa = null;
                                                for (Visa v : myVisas) {
                                                    if (v.getId() == visaIdForEntry) {
                                                        selectedVisa = v;
                                                        break;
                                                    }
                                                }

                                                if (selectedVisa == null) {
                                                    System.out.println("Invalid visa ID.");
                                                    break;
                                                }

                                                if (selectedVisa.getStatus() != VisaStatus.ACTIVE) {
                                                    System.out.println("Visa status is not ACTIVE. Current status: "
                                                            + selectedVisa.getStatus());
                                                    break;
                                                }

                                                if (selectedVisa.getExpiryDate().isBefore(LocalDate.now())) {
                                                    System.out.println(
                                                            "Visa has expired on " + selectedVisa.getExpiryDate()
                                                                    + ". Cannot request entry.");
                                                    break;
                                                }

                                                System.out.print(
                                                        "Enter entry point (e.g., Airport, Seaport, Land Border): ");
                                                String entryPoint = sc.nextLine();

                                                java.time.LocalDateTime entryTime = java.time.LocalDateTime.now();
                                                System.out.println("Entry date and time recorded: " + entryTime);

                                                long recordId = entryRecords.size() + 1;
                                                BorderEntryRecord entryRecord = new BorderEntryRecord(
                                                        recordId,
                                                        currentVisitor,
                                                        selectedVisa,
                                                        entryTime,
                                                        entryPoint,
                                                        null,
                                                        BorderEntryStatus.IN_COUNTRY,
                                                        null);

                                                BSFFunctions.writeBorderEntryRecordDataToExcel(entryRecord);
                                                entryRecords.add(entryRecord);
                                                // Send notification for entry approved
                                                NotificationManager.notifyEntryApproved(currentVisitor, entryRecord, notifications);
                                                System.out.println(
                                                        "Entry request submitted successfully. Record ID: " + recordId);
                                                break;
                                            case 6:
                                                BSFFunctions.displayNotificationsForUser(loggedInUser, notifications);
                                                break;
                                            case 7:
                                                System.out.println("Logged out.");
                                                break;
                                            default:
                                                System.out.println("Invalid choice. Please try again.");
                                        }
                                    }
                                    break;

                                case ADMIN:
                                    System.out.println("Welcome, " + loggedInUser.getUsername() + "! You are an admin.");

                                    int adminChoice = 0;

                                    while (adminChoice != 9) {
                                        System.out.println("1. View all visa applications");
                                        System.out.println("2. View all visitors");
                                        System.out.println("3. View all users");
                                        System.out.println("4. View all visas");
                                        System.out.println("5. View border entry requests");
                                        System.out.println("6. View All Notifications");
                                        System.out.println("7. View All Warrants");
                                        System.out.println("8. Send Notifications");
                                        System.out.println("9. Logout");

                                        System.out.print("Enter your choice: ");
                                        adminChoice = sc.nextInt();
                                        sc.nextLine();

                                        switch (adminChoice) {
                                            case 1:
                                                BSFFunctions.displayApplication(loggedInUser);

                                                System.out.print("Enter the ID of the application you want to process (or press 0 to go back): ");
                                                int appId = sc.nextInt();
                                                sc.nextLine();
                                                VisaApplication applicationToProcess = null;
                                                
                                                if (appId == 0) break;
                                                
                                                for (VisaApplication app1 : applications) {
                                                    if (app1.getId() == appId) {
                                                        applicationToProcess = app1;
                                                        break;
                                                    }
                                                }
                                                if (applicationToProcess == null) {
                                                    System.out.println("Application not found.");
                                                    break;
                                                }
                                                System.out.println("1. Approve");
                                                System.out.println("2. Reject");
                                                System.out.print("Enter your decision: ");
                                                int decision = sc.nextInt();
                                                sc.nextLine();
                                                if (decision == 1) {
                                                    applicationToProcess.setStatus(ApplicationStatus.APPROVED);
                                                    applicationToProcess.setProcessedBy(loggedInUser);
                                                    BSFFunctions.updateCellInExcel(3, applicationToProcess.getId(), 0,
                                                            5,
                                                            "APPROVED");

                                                    long visaId = visas.size() + 100;
                                                    String visaNumber = "V" + (100000 + (int) (Math.random() * 900000));
                                                    Visitor holder = applicationToProcess.getApplicant();
                                                    VisaType vType = applicationToProcess.getVisaType();
                                                    LocalDate issueDate = LocalDate.now();
                                                    System.out.println("Enter the number if weeks for visa validity (e.g., 4 for 4 weeks): ");
                                                    int weeks = sc.nextInt();
                                                    sc.nextLine();
                                                    LocalDate expiryDate = issueDate.plusWeeks(weeks);
                                                    int maxStayDays = 3;
                                                    VisaStatus vStatus = VisaStatus.ACTIVE;
                                                    Visa visa = new Visa(visaId, visaNumber, holder, vType, issueDate,
                                                            expiryDate, maxStayDays, vStatus, applicationToProcess);
                                                    visas.add(visa);
                                                    if (holder != null) {
                                                        try {
                                                            holder.addVisa(visa);
                                                        } catch (Exception ex) {

                                                        }
                                                    }

                                                    BSFFunctions.writeVisaDataToExcel(visa);
                                                    BSFFunctions.updateCellInExcel(3, applicationToProcess.getId(), 0,9, visaNumber);
                                                    // Send notification for visa application approved
                                                    NotificationManager.notifyVisaApplicationApproved(holder, visaNumber, notifications);
                                                    System.out.println("Application approved. Visa generated: " + visaNumber);

                                                } else if (decision == 2) {
                                                    applicationToProcess.setStatus(ApplicationStatus.REJECTED);
                                                    applicationToProcess.setProcessedBy(loggedInUser);
                                                    System.out.print("Enter reason for rejection: ");
                                                    String rejReason = sc.nextLine();
                                                    applicationToProcess.setRejectionReason(rejReason);
                                                    BSFFunctions.updateCellInExcel(3, applicationToProcess.getId(), 0,5, "REJECTED");
                                                    BSFFunctions.updateCellInExcel(3, applicationToProcess.getId(), 0,7, rejReason);
                                                    // Send notification for visa application rejected
                                                    Visitor applicantVisitor = applicationToProcess.getApplicant();
                                                    NotificationManager.notifyVisaApplicationRejected(applicantVisitor, rejReason, notifications);
                                                    System.out.println("Application rejected.");
                                                } else {
                                                    System.out.println("Invalid decision.");
                                                }
                                                break;
                                            case 2:
                                                BSFFunctions.displayAllVisitors(visitors);
                                                break;
                                            case 3:
                                                BSFFunctions.displayAllUsersForAdmin(users);
                                                break;
                                            case 4:
                                                BSFFunctions.displayAllVisas();
                                                break;
                                            case 5:
                                                BSFFunctions.displayBorderEntryRecords(loggedInUser);

                                                if (entryRecords.isEmpty()) {
                                                    System.out.println("No border entry records to process.");
                                                    break;
                                                }

                                                System.out.print("Enter the ID of the border entry record you want to process (or 0 to skip): ");
                                                long entryRecordId = sc.nextLong();
                                                sc.nextLine();

                                                if (entryRecordId == 0)break;

                                                BorderEntryRecord recordToProcess = null;
                                                for (BorderEntryRecord r : entryRecords) {
                                                    if (r.getId() == entryRecordId) {
                                                        recordToProcess = r;
                                                        break;
                                                    }
                                                }

                                                if (recordToProcess == null) {
                                                    System.out.println("Border entry record not found.");
                                                    break;
                                                }

                                                System.out.println("1. Record Exit");
                                                System.out.println("2. Mark as Overstay");
                                                System.out.print("Enter your choice: ");
                                                int entryDecision = sc.nextInt();
                                                sc.nextLine();

                                                if (entryDecision == 1) {
                                                    System.out.print(
                                                            "Enter exit date and time (format: YYYY-MM-DD HH:MM:SS): ");
                                                    String exitTimeStr = sc.nextLine();
                                                    java.time.LocalDateTime exitTime;
                                                    try {
                                                        exitTime = java.time.LocalDateTime.parse(exitTimeStr,
                                                                java.time.format.DateTimeFormatter
                                                                        .ofPattern("yyyy-MM-dd HH:mm:ss"));
                                                    } catch (Exception e) {
                                                        System.out.println("Invalid date format. Using current time.");
                                                        exitTime = java.time.LocalDateTime.now();
                                                    }

                                                    recordToProcess.setExitTime(exitTime);
                                                    recordToProcess.setStatus(BorderEntryStatus.EXITED);
                                                    recordToProcess.setProcessedBy(loggedInUser);

                                                    BSFFunctions.updateCellInExcel(5, entryRecordId, 0, 6,
                                                            exitTime.toString());
                                                    BSFFunctions.updateCellInExcel(5, entryRecordId, 0, 7, "EXITED");
                                                    
                                                    // Send notification for exit confirmed
                                                    Visitor exitingVisitor = recordToProcess.getVisitor();
                                                    if (exitingVisitor != null) {
                                                        NotificationManager.notifyExitConfirmed(exitingVisitor, recordToProcess, notifications);
                                                    }
                                                    
                                                    // Close any active warrants related to this entry and persist the change
                                                    for (Warrant w : warrants) {
                                                        if (w == null) continue;
                                                        try {
                                                            if (w.getRelatedEntry() != null && w.getRelatedEntry().getId() == entryRecordId) {
                                                                if (w.getStatus() != WarrantStatus.CLOSED) {
                                                                    w.setStatus(WarrantStatus.CLOSED);
                                                                    // warrants sheet index is 8, status column index is 5
                                                                    BSFFunctions.updateCellInExcel(8, w.getId(), 0, 5, "CLOSED");
                                                                    // Send notification for warrant closed
                                                                    NotificationManager.notifyWarrantClosed(exitingVisitor, w, notifications);
                                                                }
                                                            }
                                                        } catch (Exception ex) {
                                                            // ignore individual warrant update errors
                                                        }
                                                    }
                                                    System.out.println("Exit recorded successfully. Related warrants (if any) closed.");
                                                } else if (entryDecision == 2) {
                                                    recordToProcess.setStatus(BorderEntryStatus.OVERSTAY);
                                                    recordToProcess.setProcessedBy(loggedInUser);
                                                    BSFFunctions.updateCellInExcel(5, entryRecordId, 0, 7, "OVERSTAY");
                                                    System.out.println("Entry marked as OVERSTAY.");
                                                }
                                                break;
                                            case 6:
                                                // First, display all users
                                                BSFFunctions.displayAllUsers(users);
                                                
                                                // Ask admin to select a user
                                                System.out.print("Enter the ID of the user to view their notifications (or 0 to skip): ");
                                                long selectedUserId = sc.nextLong();
                                                sc.nextLine();
                                                
                                                if (selectedUserId == 0) {
                                                    break;
                                                }
                                                
                                                User selectedUser = null;
                                                for (User user1 : users) {
                                                    if (user1.getId() == selectedUserId) {
                                                        selectedUser = user1;
                                                        break;
                                                    }
                                                }
                                                
                                                if (selectedUser == null) {
                                                    System.out.println("User not found.");
                                                } else {
                                                    BSFFunctions.displayNotificationsForUser(selectedUser, notifications);
                                                }
                                                break;
                                            case 7:
                                                // First, display all users
                                                BSFFunctions.displayAllUsers(users);
                                                
                                                System.out.print("Enter the ID of the user to view their warrants (or 0 to skip): ");
                                                long selectedUserIdWarrant = sc.nextLong();
                                                sc.nextLine();
                                                
                                                if (selectedUserIdWarrant == 0) {
                                                    break;
                                                }
                                                
                                                User selectedUserWarrant = null;
                                                for (User user1 : users) {
                                                    if (user1.getId() == selectedUserIdWarrant) {
                                                        selectedUserWarrant = user1;
                                                        break;
                                                    }
                                                }
                                                
                                                if (selectedUserWarrant == null) {
                                                    System.out.println("User not found.");
                                                } else {
                                                    BSFFunctions.displayWarrantsForUser(selectedUserWarrant, warrants);
                                                }
                                                break;
                                            case 8:
                                                System.out.println("\n1. Send notification to a specific user");
                                                System.out.println("2. Send notification to all users");
                                                System.out.print("Enter your choice: ");
                                                int notifyChoice = sc.nextInt();
                                                sc.nextLine();

                                                if (notifyChoice == 1) {
                                                    // Display all users and let admin select
                                                    BSFFunctions.displayAllUsers(users);
                                                    System.out.print("Enter the ID of the user to send notification to: ");
                                                    long recipientUserId = sc.nextLong();
                                                    sc.nextLine();

                                                    User recipientUser = null;
                                                    for (User user1 : users) {
                                                        if (user1.getId() == recipientUserId) {
                                                            recipientUser = user1;
                                                            break;
                                                        }
                                                    }

                                                    if (recipientUser == null) {
                                                        System.out.println("User not found.");
                                                        break;
                                                    }

                                                    // Find the visitor associated with this user
                                                    Visitor recipientVisitor = null;
                                                    for (Visitor v : visitors) {
                                                        if (v.getUserAccount().getId() == recipientUser.getId()) {
                                                            recipientVisitor = v;
                                                            break;
                                                        }
                                                    }

                                                    if (recipientVisitor == null) {
                                                        System.out.println("No visitor record found for this user.");
                                                        break;
                                                    }

                                                    System.out.print("Enter notification subject/type (VISA_EXPIRY_WARNING/OVERSTAY_WARNING/WARRANT_CREATED): ");
                                                    String typeStr = sc.nextLine().toUpperCase();
                                                    NotificationType notifType;
                                                    try {
                                                        notifType = NotificationType.valueOf(typeStr);
                                                    } catch (IllegalArgumentException e) {
                                                        System.out.println("Invalid notification type. Using VISA_EXPIRY_WARNING.");
                                                        notifType = NotificationType.VISA_EXPIRY_WARNING;
                                                    }

                                                    System.out.print("Enter notification message: ");
                                                    String notifMessage = sc.nextLine();

                                                    // Create and send notification
                                                    Notification notif = new Notification(
                                                            recipientVisitor,
                                                            notifType,
                                                            notifMessage,
                                                            java.time.LocalDateTime.now(),
                                                            false,
                                                            false
                                                    );
                                                    notifications.add(notif);
                                                    BSFFunctions.writeNotificationToExcel(notif);
                                                    System.out.println("Notification sent to " + recipientUser.getUsername() + " and saved to Excel.");

                                                } else if (notifyChoice == 2) {
                                                    System.out.print("Enter notification subject/type (VISA_EXPIRY_WARNING/OVERSTAY_WARNING/WARRANT_CREATED): ");
                                                    String typeStrAll = sc.nextLine().toUpperCase();
                                                    NotificationType notifTypeAll;
                                                    try {
                                                        notifTypeAll = NotificationType.valueOf(typeStrAll);
                                                    } catch (IllegalArgumentException e) {
                                                        System.out.println("Invalid notification type. Using VISA_EXPIRY_WARNING.");
                                                        notifTypeAll = NotificationType.VISA_EXPIRY_WARNING;
                                                    }

                                                    System.out.print("Enter notification message: ");
                                                    String notifMessageAll = sc.nextLine();

                                                    // Send to all users who have visitors
                                                    for (Visitor v : visitors) {
                                                        Notification notif = new Notification(
                                                                v,
                                                                notifTypeAll,
                                                                notifMessageAll,
                                                                java.time.LocalDateTime.now(),
                                                                false,
                                                                false
                                                        );
                                                        notifications.add(notif);
                                                        BSFFunctions.writeNotificationToExcel(notif);
                                                    }
                                                    System.out.println("Notification sent to all " + visitors.size() + " visitors and saved to Excel.");
                                                } else {
                                                    System.out.println("Invalid choice.");
                                                }
                                                break;
                                            case 9:
                                                System.out.println("Logged out.");
                                                break;

                                            default:
                                                System.out.println("Invalid choice. Please try again.");
                                        }
                                    }
                                    break;
                            }
                            break;
                        }
                    }

                    if (!found) {
                        System.out.println("Invalid username or password. Please try again.");
                    }
                    break;

                case 2:
                    System.out.print("Enter desired username: ");
                    name = sc.nextLine().trim();
                    // check duplicate username
                    boolean exists = false;
                    for (User u : users) {
                        if (u.getUsername() != null && u.getUsername().equalsIgnoreCase(name)) {
                            exists = true;
                            break;
                        }
                    }
                    if (exists) {
                        System.out.println("Username already exists. Choose another username.");
                        break;
                    }
                    System.out.print("Enter desired password: ");
                    password = sc.nextLine();
                    System.out.print("Enter your email: ");
                    String email = sc.nextLine();
                    User newUser = new User(name, password, UserRole.APPLICANT, email);
                    users.add(newUser);
                    BSFFunctions.writeUserDataToExcel(newUser);
                    // Send notification for account registered
                    NotificationManager.notifyAccountRegistered(newUser, notifications);
                    System.out.println("Registration successful! You can now log in.");
                    break;

                case 3:
                    System.out.println("Exiting the system. Goodbye!");
                    // stop background services
                    try {
                        notificationService.stop();
                    } catch (Exception ignored) {}
                    // exit loop
                    sc.close();
                    return;

                default:
                    System.out.println("Invalid choice. Exiting.");
                    break;
            }
        }
    }
}
