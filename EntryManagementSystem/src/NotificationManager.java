import java.time.LocalDateTime;
import java.util.List;

/**
 * NotificationManager handles creation and dispatch of all notification types
 * in the Border Entry Management System
 */
public class NotificationManager {

    /**
     * Create and send visa application submitted notification
     */
    public static void notifyVisaApplicationSubmitted(Visitor visitor, VisaApplication application, List<Notification> notifications) {
        Notification notif = new Notification(
            visitor,
            NotificationType.VISA_APPLICATION_SUBMITTED,
            Notification.generateDefaultMessage(NotificationType.VISA_APPLICATION_SUBMITTED, visitor),
            LocalDateTime.now(),
            false,
            false
        );
        notifications.add(notif);
        BSFFunctions.writeNotificationToExcel(notif);
        BSFFunctions.sendNotification(notif);
        System.out.println("✓ Notification sent: Visa application submitted for " + visitor.getUserAccount().getUsername());
    }

    /**
     * Create and send visa application approved notification
     */
    public static void notifyVisaApplicationApproved(Visitor visitor, String visaNumber, List<Notification> notifications) {
        String customMessage = "Your visa application has been approved. Visa Number: " + visaNumber + 
            ". Please collect your visa from the immigration office.";
        Notification notif = new Notification(
            visitor,
            NotificationType.VISA_APPLICATION_APPROVED,
            customMessage,
            LocalDateTime.now(),
            false,
            false
        );
        notifications.add(notif);
        BSFFunctions.writeNotificationToExcel(notif);
        BSFFunctions.sendNotification(notif);
        System.out.println("✓ Notification sent: Visa approved for " + visitor.getUserAccount().getUsername());
    }

    /**
     * Create and send visa application rejected notification
     */
    public static void notifyVisaApplicationRejected(Visitor visitor, String rejectionReason, List<Notification> notifications) {
        String customMessage = "Your visa application has been rejected. Reason: " + rejectionReason;
        Notification notif = new Notification(
            visitor,
            NotificationType.VISA_APPLICATION_REJECTED,
            customMessage,
            LocalDateTime.now(),
            false,
            false
        );
        notifications.add(notif);
        BSFFunctions.writeNotificationToExcel(notif);
        BSFFunctions.sendNotification(notif);
        System.out.println("✓ Notification sent: Visa application rejected for " + visitor.getUserAccount().getUsername());
    }

    /**
     * Create and send visa expiry warning notification
     */
    public static void notifyVisaExpiryWarning(Visitor visitor, Visa visa, List<Notification> notifications) {
        String customMessage = "Your visa will expire on " + visa.getExpiryDate() + 
            ". Please renew your visa or make arrangements to depart.";
        Notification notif = new Notification(
            visitor,
            NotificationType.VISA_EXPIRY_WARNING,
            customMessage,
            LocalDateTime.now(),
            false,
            false
        );
        notifications.add(notif);
        BSFFunctions.writeNotificationToExcel(notif);
        BSFFunctions.sendNotification(notif);
        System.out.println("✓ Notification sent: Visa expiry warning for " + visitor.getUserAccount().getUsername());
    }

    /**
     * Create and send visa expired notification
     */
    public static void notifyVisaExpired(Visitor visitor, Visa visa, List<Notification> notifications) {
        String customMessage = "Your visa expired on " + visa.getExpiryDate() + 
            ". You must depart the country immediately.";
        Notification notif = new Notification(
            visitor,
            NotificationType.VISA_EXPIRED,
            customMessage,
            LocalDateTime.now(),
            false,
            false
        );
        notifications.add(notif);
        BSFFunctions.writeNotificationToExcel(notif);
        BSFFunctions.sendNotification(notif);
        System.out.println("✓ Notification sent: Visa expired for " + visitor.getUserAccount().getUsername());
    }

    /**
     * Create and send visa activated notification (upon border entry)
     */
    public static void notifyVisaActivated(Visitor visitor, BorderEntryRecord entry, List<Notification> notifications) {
        String customMessage = "Your visa has been activated. Entry time: " + entry.getEntryTime() + 
            ". Entry point: " + entry.getEntryPoint();
        Notification notif = new Notification(
            visitor,
            NotificationType.VISA_ACTIVATED,
            customMessage,
            LocalDateTime.now(),
            false,
            false
        );
        notifications.add(notif);
        BSFFunctions.writeNotificationToExcel(notif);
        BSFFunctions.sendNotification(notif);
        System.out.println("✓ Notification sent: Visa activated for " + visitor.getUserAccount().getUsername());
    }

    /**
     * Create and send entry approved notification
     */
    public static void notifyEntryApproved(Visitor visitor, BorderEntryRecord entry, List<Notification> notifications) {
        String customMessage = "Your entry has been approved. You may proceed. Entry point: " + entry.getEntryPoint();
        Notification notif = new Notification(
            visitor,
            NotificationType.ENTRY_APPROVED,
            customMessage,
            LocalDateTime.now(),
            false,
            false
        );
        notifications.add(notif);
        BSFFunctions.writeNotificationToExcel(notif);
        BSFFunctions.sendNotification(notif);
        System.out.println("✓ Notification sent: Entry approved for " + visitor.getUserAccount().getUsername());
    }

    /**
     * Create and send entry denied notification
     */
    public static void notifyEntryDenied(Visitor visitor, String reason, List<Notification> notifications) {
        String customMessage = "Your entry has been denied. Reason: " + reason;
        Notification notif = new Notification(
            visitor,
            NotificationType.ENTRY_DENIED,
            customMessage,
            LocalDateTime.now(),
            false,
            false
        );
        notifications.add(notif);
        BSFFunctions.writeNotificationToExcel(notif);
        BSFFunctions.sendNotification(notif);
        System.out.println("✓ Notification sent: Entry denied for " + visitor.getUserAccount().getUsername());
    }

    /**
     * Create and send exit confirmed notification
     */
    public static void notifyExitConfirmed(Visitor visitor, BorderEntryRecord entry, List<Notification> notifications) {
        String customMessage = "Your exit has been confirmed on " + entry.getExitTime() + 
            ". Thank you for visiting our country.";
        Notification notif = new Notification(
            visitor,
            NotificationType.EXIT_CONFIRMED,
            customMessage,
            LocalDateTime.now(),
            false,
            false
        );
        notifications.add(notif);
        BSFFunctions.writeNotificationToExcel(notif);
        BSFFunctions.sendNotification(notif);
        System.out.println("✓ Notification sent: Exit confirmed for " + visitor.getUserAccount().getUsername());
    }

    /**
     * Create and send overstay warning notification
     */
    public static void notifyOverstayWarning(Visitor visitor, BorderEntryRecord entry, Visa visa, int daysOverdue, List<Notification> notifications) {
        String customMessage = "You have exceeded your allowed stay by " + daysOverdue + " days. " +
            "Max allowed stay: " + visa.getMaxStayDays() + " days. " +
            "Please contact immigration authorities immediately.";
        Notification notif = new Notification(
            visitor,
            NotificationType.OVERSTAY_WARNING,
            customMessage,
            LocalDateTime.now(),
            false,
            false
        );
        notifications.add(notif);
        BSFFunctions.writeNotificationToExcel(notif);
        BSFFunctions.sendNotification(notif);
        System.out.println("✓ Notification sent: Overstay warning for " + visitor.getUserAccount().getUsername());
    }

    /**
     * Create and send warrant created notification
     */
    public static void notifyWarrantCreated(Visitor visitor, Warrant warrant, List<Notification> notifications) {
        String customMessage = "A warrant has been issued for your overstay violation. " +
            "Warrant ID: " + warrant.getId() + ". Reason: " + warrant.getReason() + 
            ". Please resolve this matter immediately.";
        Notification notif = new Notification(
            visitor,
            NotificationType.WARRANT_CREATED,
            customMessage,
            LocalDateTime.now(),
            false,
            false
        );
        notifications.add(notif);
        BSFFunctions.writeNotificationToExcel(notif);
        BSFFunctions.sendNotification(notif);
        System.out.println("✓ Notification sent: Warrant created for " + visitor.getUserAccount().getUsername());
    }

    /**
     * Create and send warrant closed notification
     */
    public static void notifyWarrantClosed(Visitor visitor, Warrant warrant, List<Notification> notifications) {
        String customMessage = "Your warrant (ID: " + warrant.getId() + ") has been closed. " +
            "Your case has been resolved.";
        Notification notif = new Notification(
            visitor,
            NotificationType.WARRANT_CLOSED,
            customMessage,
            LocalDateTime.now(),
            false,
            false
        );
        notifications.add(notif);
        BSFFunctions.writeNotificationToExcel(notif);
        BSFFunctions.sendNotification(notif);
        System.out.println("✓ Notification sent: Warrant closed for " + visitor.getUserAccount().getUsername());
    }

    /**
     * Create and send account registered notification
     */
    public static void notifyAccountRegistered(User user, List<Notification> notifications) {
        // Create a minimal Visitor wrapper so notifications and sender can reference a recipient
        Visitor visitor = new Visitor(user, "", "");
        Notification notif = new Notification(
            visitor,
            NotificationType.ACCOUNT_REGISTERED,
            Notification.generateDefaultMessage(NotificationType.ACCOUNT_REGISTERED, visitor),
            LocalDateTime.now(),
            false,
            false
        );
        notifications.add(notif);
        BSFFunctions.writeNotificationToExcel(notif);
        System.out.println("✓ Notification created for account registration: " + user.getEmail());
        // Attempt to send email if SMTP credentials are available
        try {
            boolean sent = BSFFunctions.sendNotification(notif);
            if (sent) System.out.println("✓ Registration email sent to: " + user.getEmail());
        } catch (Exception ignored) {}
    }

    /**
     * Create and send account login alert notification
     */
    public static void notifyLoginAlert(User user, List<Notification> notifications) {
        Visitor visitor = new Visitor(user, "", "");
        Notification notif = new Notification(
            visitor,
            NotificationType.ACCOUNT_LOGIN_ALERT,
            Notification.generateDefaultMessage(NotificationType.ACCOUNT_LOGIN_ALERT, visitor),
            LocalDateTime.now(),
            false,
            false
        );
        notifications.add(notif);
        BSFFunctions.writeNotificationToExcel(notif);
        System.out.println("✓ Login alert created for: " + user.getEmail());
        try {
            boolean sent = BSFFunctions.sendNotification(notif);
            if (sent) System.out.println("✓ Login alert email sent to: " + user.getEmail());
        } catch (Exception ignored) {}
    }
}
