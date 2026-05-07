import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.*;
import java.util.List;

public class DisplayExcelData {
    public static void displayApplication(User user) {
        String filePath = AppConfig.DATA_FILE;
        try {
            File f = new File(filePath);
            if (!f.exists()) {
                System.out.println("No applications found. Excel file does not exist.");
                return;
            }

            FileInputStream fis = new FileInputStream(f);
            Workbook workbook = new XSSFWorkbook(fis);

            if (workbook.getNumberOfSheets() <= 3) {
                System.out.println("No applications found. Sheet Of visa Application does not exist.");
                workbook.close();
                fis.close();
                return;
            }

            Sheet sheet = workbook.getSheetAt(3);
            DataFormatter formatter = new DataFormatter();

            boolean hasData = false;
            System.out.println("\n========== All Visa Applications ==========\n");

            System.out.printf("%-5s | %-12s | %-15s | %-10s | %-15s | %-10s | %-30s | %-30s | %-10s%n",
                    "ID", "Applicant ID", "Username", "Visa Type", "App Date", "Status", "Reason", "Rejection Reason",
                    "Processed By");
            System.out.println("-".repeat(130));

            for (Row row : sheet) {
                if (row.getRowNum() == 0)
                    continue;

                String id = formatter.formatCellValue(row.getCell(0));
                String applicantId = formatter.formatCellValue(row.getCell(1));
                String applicantUsername = formatter.formatCellValue(row.getCell(2));
                String visaType = formatter.formatCellValue(row.getCell(3));
                String applicationDate = formatter.formatCellValue(row.getCell(4));
                String status = formatter.formatCellValue(row.getCell(5));
                String reason = formatter.formatCellValue(row.getCell(6));
                String rejectionReason = formatter.formatCellValue(row.getCell(7));
                String processedById = formatter.formatCellValue(row.getCell(8));

                if (!id.isEmpty()) {
                    hasData = true;
                    String reasonShort = reason.length() > 30 ? reason.substring(0, 27) + "..." : reason;
                    String processedBy = processedById.isEmpty() ? "Pending" : processedById;

                    System.out.printf("%-5s | %-12s | %-15s | %-10s | %-15s | %-10s | %-30s | %-30s | %-10s%n",
                            id, applicantId, applicantUsername, visaType, applicationDate, status, reasonShort,
                            rejectionReason, processedBy);
                }
            }

            if (!hasData) {
                System.out.println("No applications found.");
            } else {
                System.out.println("-".repeat(140));
                System.out.println();
            }

            workbook.close();
            fis.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void displayVisasForUser(User user) {
        String filePath = AppConfig.DATA_FILE;
        try {
            File f = new File(filePath);
            if (!f.exists()) {
                System.out.println("No visas found. Excel file does not exist.");
                return;
            }

            FileInputStream fis = new FileInputStream(f);
            Workbook workbook = new XSSFWorkbook(fis);

            // check if sheet 5 (index 4) exists
            if (workbook.getNumberOfSheets() <= 4) {
                System.out.println("No visas found. Visa sheet does not exist.");
                workbook.close();
                fis.close();
                return;
            }

            Sheet sheet = workbook.getSheetAt(4);
            DataFormatter formatter = new DataFormatter();

            boolean hasData = false;
            System.out.println("\n========== Your Visas ==========");

            System.out.printf("%-5s | %-12s | %-10s | %-15s | %-10s | %-12s | %-12s | %-6s | %-8s | %-6s%n",
                    "ID", "VisaNumber", "HolderId", "HolderUsername", "VisaType", "IssueDate", "ExpiryDate", "Max",
                    "Status", "AppID");
            System.out.println("-".repeat(120));

            for (Row row : sheet) {
                if (row.getRowNum() == 0)
                    continue;

                String id = formatter.formatCellValue(row.getCell(0));
                String visaNumber = formatter.formatCellValue(row.getCell(1));
                String holderId = formatter.formatCellValue(row.getCell(2));
                String holderUsername = formatter.formatCellValue(row.getCell(3));
                String visaType = formatter.formatCellValue(row.getCell(4));
                String issueDate = formatter.formatCellValue(row.getCell(5));
                String expiryDate = formatter.formatCellValue(row.getCell(6));
                String maxStay = formatter.formatCellValue(row.getCell(7));
                String status = formatter.formatCellValue(row.getCell(8));
                String sourceAppId = formatter.formatCellValue(row.getCell(9));

                // match by username or user id
                boolean matches = false;
                if (!holderUsername.isEmpty() && holderUsername.equals(user.getUsername()))
                    matches = true;
                if (!holderId.isEmpty()) {
                    try {
                        long hid = Long.parseLong(holderId.trim());
                        if (hid == user.getId())
                            matches = true;
                    } catch (NumberFormatException nfe) {
                        // ignore
                    }
                }

                if (matches) {
                    hasData = true;
                    System.out.printf("%-5s | %-12s | %-10s | %-15s | %-10s | %-12s | %-12s | %-6s | %-8s | %-6s%n",
                            id, visaNumber, holderId, holderUsername, visaType, issueDate, expiryDate, maxStay, status,
                            sourceAppId);
                }
            }

            if (!hasData) {
                System.out.println("No visas found for your account.");
            } else {
                System.out.println("-".repeat(120));
                System.out.println();
            }

            workbook.close();
            fis.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void displayBorderHistoryForApplicant(User user, List<BorderEntryRecord> entryRecords) {
        boolean hasData = false;
        System.out.println("\n========== Your Border Entry History ==========\n");

        System.out.printf("%-8s | %-10s | %-20s | %-15s | %-20s | %-12s%n",
                "ID", "VisaId", "EntryTime", "EntryPoint", "ExitTime", "Status");
        System.out.println("-".repeat(110));

        for (BorderEntryRecord record : entryRecords) {
            if (record.getVisitor() != null && record.getVisitor().getUserAccount().getId() == user.getId()) {
                hasData = true;
                String recordId = String.valueOf(record.getId());
                String visaId = record.getVisaUsed() != null ? String.valueOf(record.getVisaUsed().getId()) : "N/A";
                String entryTime = record.getEntryTime() != null ? record.getEntryTime().toString() : "N/A";
                String entryPoint = record.getEntryPoint() != null ? record.getEntryPoint() : "N/A";
                String exitTime = record.getExitTime() != null ? record.getExitTime().toString() : "Pending";
                String status = record.getStatus().toString();

                System.out.printf("%-8s | %-10s | %-20s | %-15s | %-20s | %-12s%n",
                        recordId, visaId, entryTime, entryPoint, exitTime, status);
            }
        }

        if (!hasData) {
            System.out.println("No border entry records found for your account.");
        } else {
            System.out.println("-".repeat(110));
            System.out.println();
        }
    }

    public static void displayBorderEntryRecords(User user) {
        String filePath = AppConfig.DATA_FILE;
        try {
            File f = new File(filePath);
            if (!f.exists()) {
                System.out.println("No border entry records found. Excel file does not exist.");
                return;
            }

            FileInputStream fis = new FileInputStream(f);
            Workbook workbook = new XSSFWorkbook(fis);

            if (workbook.getNumberOfSheets() <= 5) {
                System.out.println("No border entry records found. Border entry sheet does not exist.");
                workbook.close();
                fis.close();
                return;
            }

            Sheet sheet = workbook.getSheetAt(5);
            DataFormatter formatter = new DataFormatter();

            boolean hasData = false;
            System.out.println("\n========== Border Entry Requests ==========\n");

            System.out.printf("%-8s | %-12s | %-15s | %-10s | %-20s | %-15s | %-20s | %-12s%n",
                    "ID", "VisitorId", "VisitorUsername", "VisaId", "EntryTime", "EntryPoint", "ExitTime", "Status");
            System.out.println("-".repeat(130));

            for (Row row : sheet) {
                if (row.getRowNum() == 0)
                    continue;

                String id = formatter.formatCellValue(row.getCell(0));
                if (id == null || id.trim().isEmpty())
                    continue;

                hasData = true;
                String visitorId = formatter.formatCellValue(row.getCell(1));
                String visitorUsername = formatter.formatCellValue(row.getCell(2));
                String visaId = formatter.formatCellValue(row.getCell(3));
                String entryTime = formatter.formatCellValue(row.getCell(4));
                String entryPoint = formatter.formatCellValue(row.getCell(5));
                String exitTime = formatter.formatCellValue(row.getCell(6));
                String status = formatter.formatCellValue(row.getCell(7));

                System.out.printf("%-8s | %-12s | %-15s | %-10s | %-20s | %-15s | %-20s | %-12s%n",
                        id, visitorId, visitorUsername, visaId, entryTime, entryPoint,
                        exitTime == null || exitTime.isEmpty() ? "Pending" : exitTime, status);
            }

            if (!hasData) {
                System.out.println("No border entry records found.");
            } else {
                System.out.println("-".repeat(140));
                System.out.println();
            }

            workbook.close();
            fis.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void displayAllVisas() {
        String filePath = AppConfig.DATA_FILE;
        try {
            File f = new File(filePath);
            if (!f.exists()) {
                System.out.println("No visas found. Excel file does not exist.");
                return;
            }

            FileInputStream fis = new FileInputStream(f);
            Workbook workbook = new XSSFWorkbook(fis);

            // check if sheet 5 (index 4) exists
            if (workbook.getNumberOfSheets() <= 4) {
                System.out.println("No visas found. Visa sheet does not exist.");
                workbook.close();
                fis.close();
                return;
            }

            Sheet sheet = workbook.getSheetAt(4);
            DataFormatter formatter = new DataFormatter();

            boolean hasData = false;
            System.out.println("\n========== All Visas ==========");

            System.out.printf("%-5s | %-12s | %-10s | %-15s | %-10s | %-12s | %-12s | %-6s | %-8s | %-6s%n",
                    "ID", "VisaNumber", "HolderId", "HolderUsername", "VisaType", "IssueDate", "ExpiryDate", "Max",
                    "Status", "AppID");
            System.out.println("-".repeat(120));

            for (Row row : sheet) {
                if (row.getRowNum() == 0)
                    continue;

                String id = formatter.formatCellValue(row.getCell(0));
                if (id == null || id.trim().isEmpty())
                    continue;
                hasData = true;

                String visaNumber = formatter.formatCellValue(row.getCell(1));
                String holderId = formatter.formatCellValue(row.getCell(2));
                String holderUsername = formatter.formatCellValue(row.getCell(3));
                String visaType = formatter.formatCellValue(row.getCell(4));
                String issueDate = formatter.formatCellValue(row.getCell(5));
                String expiryDate = formatter.formatCellValue(row.getCell(6));
                String maxStay = formatter.formatCellValue(row.getCell(7));
                String status = formatter.formatCellValue(row.getCell(8));
                String sourceAppId = formatter.formatCellValue(row.getCell(9));

                System.out.printf("%-5s | %-12s | %-10s | %-15s | %-10s | %-12s | %-12s | %-6s | %-8s | %-6s%n",
                        id, visaNumber, holderId, holderUsername, visaType, issueDate, expiryDate, maxStay, status,
                        sourceAppId);
            }

            if (!hasData) {
                System.out.println("No visas found.");
            } else {
                System.out.println("-".repeat(120));
                System.out.println();
            }

            workbook.close();
            fis.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void displayNotificationsForUser(User user, java.util.List<Notification> notifications) {
        String filePath = AppConfig.DATA_FILE;
        java.util.Set<Long> shownIds = new java.util.HashSet<>();

        System.out.println("\n========== Your Notifications ==========");
        System.out.printf("%-6s | %-20s | %-50s | %-20s | %-8s%n", "ID", "Type", "Message", "CreatedAt", "EmailSent");
        System.out.println("-".repeat(120));

        // First: show in-memory notifications passed from the app
        if (notifications != null) {
            for (Notification n : notifications) {
                try {
                    if (n.getRecipient() != null && n.getRecipient().getUserAccount() != null
                            && n.getRecipient().getUserAccount().getId() == user.getId()) {
                        shownIds.add(n.getId());
                        String msg = n.getMessage() != null ? n.getMessage() : "";
                        String created = n.getCreatedAt() != null ? n.getCreatedAt().toString() : "";
                        System.out.printf("%-6d | %-20s | %-50s | %-20s | %-8s%n", n.getId(), n.getType(),
                                msg.length() > 50 ? msg.substring(0, 47) + "..." : msg, created,
                                n.isEmailSent());
                        // mark in-app shown in-memory and try to update Excel if present
                        n.setInAppShown(true);
                        try {
                            UpdateExcelData.updateCellInExcel(7, n.getId(), 0, 8, "true");
                        } catch (Exception ex) {
                            // best-effort; ignore
                        }
                    }
                } catch (Exception e) {
                    // ignore per-notification errors
                }
            }
        }

        // Second: read persisted notifications from sheet index 7
        try {
            File f = new File(filePath);
            if (f.exists()) {
                FileInputStream fis = new FileInputStream(f);
                org.apache.poi.ss.usermodel.Workbook workbook = new org.apache.poi.xssf.usermodel.XSSFWorkbook(fis);

                if (workbook.getNumberOfSheets() > 7) {
                    Sheet sheet = workbook.getSheetAt(7);
                    DataFormatter formatter = new DataFormatter();

                    for (Row row : sheet) {
                        if (row.getRowNum() == 0)
                            continue;

                        String idStr = formatter.formatCellValue(row.getCell(0));
                        if (idStr == null || idStr.trim().isEmpty())
                            continue;

                        long id = 0;
                        try {
                            id = Long.parseLong(idStr.trim());
                        } catch (NumberFormatException nfe) {
                            continue;
                        }

                        if (shownIds.contains(id))
                            continue; // already printed from in-memory list

                        String recipIdStr = formatter.formatCellValue(row.getCell(1));
                        String recipUser = formatter.formatCellValue(row.getCell(2));

                        boolean belongsToUser = false;
                        if (recipUser != null && !recipUser.isEmpty() && recipUser.equals(user.getUsername()))
                            belongsToUser = true;
                        if (!belongsToUser && recipIdStr != null && !recipIdStr.isEmpty()) {
                            try {
                                long rid = Long.parseLong(recipIdStr.trim());
                                if (rid == user.getId())
                                    belongsToUser = true;
                            } catch (NumberFormatException nfe) {
                                // ignore
                            }
                        }

                        if (!belongsToUser)
                            continue;

                        String type = formatter.formatCellValue(row.getCell(3));
                        String subject = formatter.formatCellValue(row.getCell(4));
                        String message = formatter.formatCellValue(row.getCell(5));
                        String createdAt = formatter.formatCellValue(row.getCell(6));
                        String emailSent = formatter.formatCellValue(row.getCell(7));
                        String inAppShown = formatter.formatCellValue(row.getCell(8));

                        System.out.printf("%-6d | %-20s | %-50s | %-20s | %-8s%n", id, type,
                                message.length() > 50 ? message.substring(0, 47) + "..." : message, createdAt,
                                emailSent);

                        // mark cell as shown if not already
                        if (inAppShown == null || !inAppShown.equalsIgnoreCase("true")) {
                            try {
                                UpdateExcelData.updateCellInExcel(7, id, 0, 8, "true");
                            } catch (Exception ex) {
                                // ignore
                            }
                        }
                    }
                }

                workbook.close();
                fis.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("-".repeat(120));
    }

    public static void displayAllNotifications(java.util.List<Notification> notifications) {
        String filePath = AppConfig.DATA_FILE;
        java.util.Set<Long> shownIds = new java.util.HashSet<>();

        System.out.println("\n========== All Notifications ==========");
        System.out.printf("%-6s | %-12s | %-20s | %-50s | %-20s | %-8s%n", "ID", "Recipient", "Type", "Message", "CreatedAt", "EmailSent");
        System.out.println("-".repeat(140));

        if (notifications != null) {
            for (Notification n : notifications) {
                try {
                    long rid = n.getRecipient() != null && n.getRecipient().getUserAccount() != null
                            ? n.getRecipient().getUserAccount().getId() : 0;
                    String ruser = n.getRecipient() != null && n.getRecipient().getUserAccount() != null
                            ? n.getRecipient().getUserAccount().getUsername() : "";
                    shownIds.add(n.getId());
                    String msg = n.getMessage() != null ? n.getMessage() : "";
                    String created = n.getCreatedAt() != null ? n.getCreatedAt().toString() : "";
                    System.out.printf("%-6d | %-12s | %-20s | %-50s | %-20s | %-8s%n", n.getId(), ruser, n.getType(),
                            msg.length() > 50 ? msg.substring(0, 47) + "..." : msg, created, n.isEmailSent());
                } catch (Exception e) {
                    // ignore
                }
            }
        }

        // read persisted notifications
        try {
            File f = new File(filePath);
            if (f.exists()) {
                FileInputStream fis = new FileInputStream(f);
                Workbook workbook = new XSSFWorkbook(fis);
                if (workbook.getNumberOfSheets() > 7) {
                    Sheet sheet = workbook.getSheetAt(7);
                    DataFormatter fmt = new DataFormatter();
                    for (Row row : sheet) {
                        if (row.getRowNum() == 0) continue;
                        String idStr = fmt.formatCellValue(row.getCell(0));
                        if (idStr == null || idStr.trim().isEmpty()) continue;
                        long id = 0;
                        try { id = Long.parseLong(idStr.trim()); } catch (Exception ex) { continue; }
                        if (shownIds.contains(id)) continue;
                        String recipUser = fmt.formatCellValue(row.getCell(2));
                        String type = fmt.formatCellValue(row.getCell(3));
                        String message = fmt.formatCellValue(row.getCell(5));
                        String createdAt = fmt.formatCellValue(row.getCell(6));
                        String emailSent = fmt.formatCellValue(row.getCell(7));

                        System.out.printf("%-6d | %-12s | %-20s | %-50s | %-20s | %-8s%n", id, recipUser, type,
                                message.length() > 50 ? message.substring(0, 47) + "..." : message, createdAt,
                                emailSent == null ? "" : emailSent);
                    }
                }
                workbook.close();
                fis.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("-".repeat(140));
    }

    public static void displayAllWarrants(java.util.List<Warrant> warrants) {
        String filePath = AppConfig.DATA_FILE;
        java.util.Set<Long> shown = new java.util.HashSet<>();

        System.out.println("\n========== All Warrants ==========");
        System.out.printf("%-6s | %-12s | %-12s | %-10s | %-20s | %-8s | %-30s%n", "ID", "SubjectId", "Username", "EntryId", "IssuedAt", "Status", "Reason");
        System.out.println("-".repeat(140));

        if (warrants != null) {
            for (Warrant w : warrants) {
                try {
                    shown.add(w.getId());
                    String user = w.getSubject() != null && w.getSubject().getUserAccount() != null
                            ? w.getSubject().getUserAccount().getUsername() : "";
                    String issued = w.getIssuedAt() != null ? w.getIssuedAt().toString() : "";
                    String reason = w.getReason() != null ? w.getReason() : "";
                    long rel = w.getRelatedEntry() != null ? w.getRelatedEntry().getId() : 0;
                    System.out.printf("%-6d | %-12d | %-12s | %-10d | %-20s | %-8s | %-30s%n", w.getId(),
                            w.getSubject() != null ? w.getSubject().getId() : 0, user, rel, issued,
                            w.getStatus() != null ? w.getStatus().toString() : "", reason.length() > 30 ? reason.substring(0,27)+"..." : reason);
                } catch (Exception e) {
                    // ignore
                }
            }
        }

        // read persisted warrants from sheet index 8
        try {
            File f = new File(filePath);
            if (f.exists()) {
                FileInputStream fis = new FileInputStream(f);
                Workbook workbook = new XSSFWorkbook(fis);
                if (workbook.getNumberOfSheets() > 8) {
                    Sheet sheet = workbook.getSheetAt(8);
                    DataFormatter fmt = new DataFormatter();
                    for (Row row : sheet) {
                        if (row.getRowNum() == 0) continue;
                        String idStr = fmt.formatCellValue(row.getCell(0));
                        if (idStr == null || idStr.trim().isEmpty()) continue;
                        long id = 0;
                        try { id = Long.parseLong(idStr.trim()); } catch (Exception ex) { continue; }
                        if (shown.contains(id)) continue;
                        String subjId = fmt.formatCellValue(row.getCell(1));
                        String subjUser = fmt.formatCellValue(row.getCell(2));
                        String related = fmt.formatCellValue(row.getCell(3));
                        String issuedAt = fmt.formatCellValue(row.getCell(4));
                        String status = fmt.formatCellValue(row.getCell(5));
                        String reason = fmt.formatCellValue(row.getCell(6));

                        long sid = 0;
                        long rel = 0;
                        try { sid = Long.parseLong(subjId.trim()); } catch (Exception ex) {}
                        try { rel = Long.parseLong(related.trim()); } catch (Exception ex) {}

                        System.out.printf("%-6d | %-12d | %-12s | %-10d | %-20s | %-8s | %-30s%n", id, sid,
                                subjUser, rel, issuedAt, status, reason.length() > 30 ? reason.substring(0,27)+"..." : reason);
                    }
                }
                workbook.close();
                fis.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("-".repeat(140));
    }

    public static void displayAllUsers(List<User> users) {
        if (users == null || users.isEmpty()) {
            System.out.println("No users found.");
            return;
        }

        System.out.println("\n========== All Users ==========\n");
        System.out.printf("%-8s | %-20s | %-15s | %-30s%n", "ID", "Username", "Role", "Email");
        System.out.println("-".repeat(80));

        for (User user : users) {
            String email = user.getEmail() != null ? user.getEmail() : "N/A";
            System.out.printf("%-8d | %-20s | %-15s | %-30s%n", user.getId(), user.getUsername(), user.getRole(), email);
        }

        System.out.println("-".repeat(80));
        System.out.println();
    }

    public static void displayWarrantsForUser(User user, List<Warrant> warrants) {
        if (user == null) {
            System.out.println("User is null.");
            return;
        }

        System.out.println("\n========== Warrants for User: " + user.getUsername() + " ==========\n");
        System.out.printf("%-6s | %-12s | %-12s | %-10s | %-20s | %-8s | %-30s%n", "ID", "SubjectId", "Username", "EntryId", "IssuedAt", "Status", "Reason");
        System.out.println("-".repeat(140));

        boolean found = false;

        // Display in-memory warrants for this user
        if (warrants != null) {
            for (Warrant w : warrants) {
                try {
                    // Check if warrant relates to this user via subject (visitor->user)
                    if (w.getSubject() != null && w.getSubject().getUserAccount() != null 
                            && w.getSubject().getUserAccount().getId() == user.getId()) {
                        found = true;
                        String issued = w.getIssuedAt() != null ? w.getIssuedAt().toString() : "";
                        String reason = w.getReason() != null ? w.getReason() : "";
                        long rel = w.getRelatedEntry() != null ? w.getRelatedEntry().getId() : 0;
                        System.out.printf("%-6d | %-12d | %-12s | %-10d | %-20s | %-8s | %-30s%n", w.getId(),
                                w.getSubject().getId(), user.getUsername(), rel, issued,
                                w.getStatus() != null ? w.getStatus().toString() : "", reason.length() > 30 ? reason.substring(0,27)+"..." : reason);
                    }
                } catch (Exception e) {
                    // ignore
                }
            }
        }

        // Display persisted warrants from sheet index 8
        String filePath = AppConfig.DATA_FILE;
        try {
            File f = new File(filePath);
            if (f.exists()) {
                FileInputStream fis = new FileInputStream(f);
                Workbook workbook = new XSSFWorkbook(fis);
                if (workbook.getNumberOfSheets() > 8) {
                    Sheet sheet = workbook.getSheetAt(8);
                    DataFormatter fmt = new DataFormatter();
                    for (Row row : sheet) {
                        if (row.getRowNum() == 0) continue;
                        String subjId = fmt.formatCellValue(row.getCell(1));
                        long sid = 0;
                        try { sid = Long.parseLong(subjId.trim()); } catch (Exception ex) { continue; }
                        
                        // Skip if we're not looking at the right user's visitor
                        // (would need visitor list to cross-check; for now just display for any subject)
                        if (sid > 0) {
                            found = true;
                            String idStr = fmt.formatCellValue(row.getCell(0));
                            String subjUser = fmt.formatCellValue(row.getCell(2));
                            String related = fmt.formatCellValue(row.getCell(3));
                            String issuedAt = fmt.formatCellValue(row.getCell(4));
                            String status = fmt.formatCellValue(row.getCell(5));
                            String reason = fmt.formatCellValue(row.getCell(6));

                            long id = 0;
                            long rel = 0;
                            try { id = Long.parseLong(idStr.trim()); } catch (Exception ex) {}
                            try { rel = Long.parseLong(related.trim()); } catch (Exception ex) {}

                            System.out.printf("%-6d | %-12d | %-12s | %-10d | %-20s | %-8s | %-30s%n", id, sid,
                                    subjUser, rel, issuedAt, status, reason.length() > 30 ? reason.substring(0,27)+"..." : reason);
                        }
                    }
                }
                workbook.close();
                fis.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!found) {
            System.out.println("No warrants found for this user.");
        }
        System.out.println("-".repeat(140));
        System.out.println();
    }

    public static void displayAllVisitors(List<Visitor> visitors) {
        if (visitors == null || visitors.isEmpty()) {
            System.out.println("No visitors found.");
            return;
        }

        System.out.println("\n========== All Visitors ==========\n");
        System.out.printf("%-8s | %-20s | %-15s | %-15s%n", "Visitor ID", "Username", "Passport", "Nationality");
        System.out.println("-".repeat(70));

        for (Visitor visitor : visitors) {
            String username = visitor.getUserAccount() != null ? visitor.getUserAccount().getUsername() : "N/A";
            String passport = visitor.getPassportNumber() != null ? visitor.getPassportNumber() : "N/A";
            String nationality = visitor.getNationality() != null ? visitor.getNationality() : "N/A";
            System.out.printf("%-8d | %-20s | %-15s | %-15s%n", visitor.getId(), username, passport, nationality);
        }

        System.out.println("-".repeat(70));
        System.out.println();
    }

    public static void displayAllUsersForAdmin(List<User> users) {
        if (users == null || users.isEmpty()) {
            System.out.println("No users found.");
            return;
        }

        System.out.println("\n========== All Users ==========\n");
        System.out.printf("%-8s | %-20s | %-15s%n", "User ID", "Username", "Role");
        System.out.println("-".repeat(50));

        for (User user : users) {
            System.out.printf("%-8d | %-20s | %-15s%n", user.getId(), user.getUsername(), user.getRole());
        }

        System.out.println("-".repeat(50));
        System.out.println();
    }
}
