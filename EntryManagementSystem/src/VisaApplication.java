import java.time.LocalDate;

enum ApplicationStatus {
    PENDING,
    APPROVED,
    REJECTED
}

enum VisaType {
    TOURIST,
    STUDENT,
    WORK,
    BUSINESS
}

class VisaApplication {
    private long id;
    static private int idCounter = 100;
    private Visitor applicant;
    private VisaType visaType;
    private LocalDate applicationDate;
    private ApplicationStatus status;
    private String Reason;
    private String rejectionReason;
    private User processedBy;

    public VisaApplication(Visitor applicant, VisaType visaType, LocalDate applicationDate, ApplicationStatus status, String Reason, String rejectionReason, User processedBy) {
        this.id = idCounter++;
        this.applicant = applicant;
        this.visaType = visaType;
        this.applicationDate = applicationDate;
        this.status = status;
        this.Reason = Reason;
        this.rejectionReason = rejectionReason;
        this.processedBy = processedBy;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Visitor getApplicant() {
        return applicant;
    }

    public void setApplicant(Visitor applicant) {
        this.applicant = applicant;
    }

    public VisaType getVisaType() {
        return visaType;
    }

    public void setVisaType(VisaType visaType) {
        this.visaType = visaType;
    }

    public LocalDate getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(LocalDate applicationDate) {
        this.applicationDate = applicationDate;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }

    public String getReason() {
        return Reason;
    }

    public void setReason(String Reason) {
        this.Reason = Reason;
    }

    public User getProcessedBy() {
        return processedBy;
    }

    public void setProcessedBy(User processedBy) {
        this.processedBy = processedBy;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    @Override
    public String toString() {
        return "VisaApplication{" +
                "id=" + id +
                ", applicant=" + applicant +
                ", visaType=" + visaType +
                ", applicationDate=" + applicationDate +
                ", status=" + status +
                ", Reason='" + Reason + '\'' +
                ", rejectionReason='" + rejectionReason + '\'' +
                ", processedBy=" + processedBy +
                '}';
    }


}
