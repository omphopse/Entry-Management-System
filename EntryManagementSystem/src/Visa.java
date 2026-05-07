import java.time.LocalDate;

enum VisaStatus {
    ACTIVE,
    EXPIRED,
    REVOKED
}

class Visa {
    private long id;
    private String visaNumber;
    private Visitor holder;
    private VisaType visaType;

    private LocalDate issueDate;
    private LocalDate expiryDate;
    private int maxStayDays; 

    private VisaStatus status; 

    private VisaApplication sourceApplication;

    public Visa(long id, String visaNumber, Visitor holder, VisaType visaType, LocalDate issueDate, LocalDate expiryDate, int maxStayDays, VisaStatus status, VisaApplication sourceApplication) {
        this.id = id;
        this.visaNumber = visaNumber;
        this.holder = holder;
        this.visaType = visaType;
        this.issueDate = issueDate;
        this.expiryDate = expiryDate;
        this.maxStayDays = maxStayDays;
        this.status = status;
        this.sourceApplication = sourceApplication;
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public String getVisaNumber() {
        return visaNumber;
    }

    public void setVisaNumber(String visaNumber) {
        this.visaNumber = visaNumber;
    }

    public Visitor getHolder() {
        return holder;
    }

    public void setHolder(Visitor holder) {
        this.holder = holder;
    }

    public VisaType getVisaType() {
        return visaType;
    }

    public void setVisaType(VisaType visaType) {
        this.visaType = visaType;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }   

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public int getMaxStayDays() {
        return maxStayDays;
    }

    public void setMaxStayDays(int maxStayDays) {
        this.maxStayDays = maxStayDays;
    }

    public VisaStatus getStatus() {
        return status;
    }

    public void setStatus(VisaStatus status) {
        this.status = status;
    }

    public VisaApplication getSourceApplication() {
        return sourceApplication;
    }

    public void setSourceApplication(VisaApplication sourceApplication) {
        this.sourceApplication = sourceApplication;
    }

    @Override
    public String toString() {
        return "Visa{" +
                "id=" + id +
                ", visaNumber='" + visaNumber + '\'' +
                ", holder=" + holder +
                ", visaType=" + visaType +
                ", issueDate=" + issueDate +
                ", expiryDate=" + expiryDate +
                ", maxStayDays=" + maxStayDays +
                ", status=" + status +
                ", sourceApplication=" + sourceApplication +
                '}';
    }

}
