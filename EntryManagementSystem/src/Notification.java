import java.time.LocalDateTime;
enum NotificationType {
    // ✅ Initial Warnings
    VISA_EXPIRY_WARNING,            // Visa about to expire
    OVERSTAY_WARNING,               // Visitor overstaying
    WARRANT_CREATED,                // Warrant issued for overstay

    // ✅ Visa Application Process
    VISA_APPLICATION_SUBMITTED,     // User applied for a visa
    VISA_APPLICATION_APPROVED,      // Admin approved the visa
    VISA_APPLICATION_REJECTED,      // Admin rejected the visa

    // ✅ Visa Validity & Expiry
    VISA_ACTIVATED,                 // Visitor used visa at border
    VISA_EXPIRED,                   // Visa has officially expired
    VISA_REVOKED,                   // Visa canceled by authority

    // ✅ Border Entry & Exit
    ENTRY_APPROVED,                 // Visitor allowed to enter
    ENTRY_DENIED,                   // Entry denied at border
    EXIT_CONFIRMED,                 // Visitor successfully exited

    // ✅ Overstay & Legal Actions
    WARRANT_CLOSED,                 // Case resolved / person exited

    // ✅ System & Security
    ACCOUNT_LOGIN_ALERT,            // Suspicious login
    ACCOUNT_REGISTERED,             // New account created
}

class Notification {
    private long id;
    static private int nextId = 100;
    private Visitor recipient;
    private NotificationType type; 
    private String message;
    private LocalDateTime createdAt;
    private boolean emailSent;
    private boolean inAppShown;

    public Notification(Visitor recipient, NotificationType type, String message, LocalDateTime createdAt, boolean emailSent, boolean inAppShown) {
        this.id = nextId++;
        this.recipient = recipient;
        this.type = type;
        this.message = message;
        this.createdAt = createdAt;
        this.emailSent = emailSent;
        this.inAppShown = inAppShown;
    }

    public long getId() {
        return id;
    }

    public Visitor getRecipient() {
        return recipient;
    }

    public void setRecipient(Visitor recipient) {
        this.recipient = recipient;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public boolean isEmailSent() {
        return emailSent;
    }

    public boolean isInAppShown() {
        return inAppShown;
    }

    public void setEmailSent(boolean emailSent) {
        this.emailSent = emailSent;
    }

    public void setInAppShown(boolean inAppShown) {
        this.inAppShown = inAppShown;
    }

    /**
     * Get subject line for notification based on type
     */
    public String getSubject() {
        switch(this.type) {
            case VISA_EXPIRY_WARNING:
                return "Visa Expiry Warning";
            case OVERSTAY_WARNING:
                return "Overstay Alert";
            case WARRANT_CREATED:
                return "Warrant Issued";
            case VISA_APPLICATION_SUBMITTED:
                return "Visa Application Received";
            case VISA_APPLICATION_APPROVED:
                return "Visa Application Approved";
            case VISA_APPLICATION_REJECTED:
                return "Visa Application Rejected";
            case VISA_ACTIVATED:
                return "Visa Activated";
            case VISA_EXPIRED:
                return "Visa Expired";
            case VISA_REVOKED:
                return "Visa Revoked";
            case ENTRY_APPROVED:
                return "Entry Approved";
            case ENTRY_DENIED:
                return "Entry Denied";
            case EXIT_CONFIRMED:
                return "Exit Confirmed";
            case WARRANT_CLOSED:
                return "Warrant Closed";
            case ACCOUNT_LOGIN_ALERT:
                return "Login Alert";
            case ACCOUNT_REGISTERED:
                return "Account Created";
            default:
                return "Notification";
        }
    }

    /**
     * Generate default message based on notification type
     */
    public static String generateDefaultMessage(NotificationType type, Visitor visitor) {
        String visitorName = visitor != null && visitor.getUserAccount() != null ? 
            visitor.getUserAccount().getUsername() : "User";
        
        switch(type) {
            case VISA_EXPIRY_WARNING:
                return "Your visa will expire soon. Please make arrangements to renew or depart before expiration.";
            case OVERSTAY_WARNING:
                return "You have exceeded your allowed stay duration. Please contact immigration authorities immediately.";
            case WARRANT_CREATED:
                return "A warrant has been issued due to your overstay violation.";
            case VISA_APPLICATION_SUBMITTED:
                return "Your visa application has been received and is under review.";
            case VISA_APPLICATION_APPROVED:
                return "Your visa application has been approved. Your visa is ready for collection.";
            case VISA_APPLICATION_REJECTED:
                return "Your visa application has been rejected. Please contact immigration for details.";
            case VISA_ACTIVATED:
                return "Your visa has been activated upon entry into the country.";
            case VISA_EXPIRED:
                return "Your visa has expired. You must depart the country immediately.";
            case VISA_REVOKED:
                return "Your visa has been revoked by immigration authorities.";
            case ENTRY_APPROVED:
                return "Your border entry has been approved. Welcome to the country.";
            case ENTRY_DENIED:
                return "Your entry request has been denied. Please contact immigration for more information.";
            case EXIT_CONFIRMED:
                return "Your exit has been confirmed. Thank you for visiting.";
            case WARRANT_CLOSED:
                return "Your warrant has been closed upon your exit from the country.";
            case ACCOUNT_LOGIN_ALERT:
                return "An unusual login attempt was detected on your account. If this wasn't you, please secure your account.";
            case ACCOUNT_REGISTERED:
                return "Your account has been successfully created. You can now log in.";
            default:
                return "You have received a notification from the Immigration System.";
        }
    }

    @Override
    public String toString() {
        return "Notification{" +
                "id=" + id +
                ", recipient=" + (recipient != null ? recipient.getId() : "null") +
                ", type=" + type +
                ", message='" + message + '\'' +
                ", createdAt=" + createdAt +
                ", emailSent=" + emailSent +
                ", inAppShown=" + inAppShown +
                '}';
    }
}
