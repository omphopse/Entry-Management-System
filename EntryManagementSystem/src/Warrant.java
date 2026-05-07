import java.time.LocalDateTime;
enum WarrantStatus {
    ACTIVE,
    CLOSED
}
class Warrant {
    private long id;
    private Visitor subject;
    private BorderEntryRecord relatedEntry;
    private LocalDateTime issuedAt;
    private WarrantStatus status; // ACTIVE, CLOSED
    private String reason; // "Overstay by 2 days" etc.

    public Warrant(long id, Visitor subject, BorderEntryRecord relatedEntry, LocalDateTime issuedAt, WarrantStatus status, String reason) {
        this.id = id;
        this.subject = subject;
        this.relatedEntry = relatedEntry;
        this.issuedAt = issuedAt;
        this.status = status;
        this.reason = reason;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Visitor getSubject() {
        return subject;
    }

    public void setSubject(Visitor subject) {
        this.subject = subject;
    }

    public BorderEntryRecord getRelatedEntry() {
        return relatedEntry;
    }

    public void setRelatedEntry(BorderEntryRecord relatedEntry) {
        this.relatedEntry = relatedEntry;
    }

    public LocalDateTime getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(LocalDateTime issuedAt) {
        this.issuedAt = issuedAt;
    }

    public WarrantStatus getStatus() {
        return status;
    }

    public void setStatus(WarrantStatus status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "Warrant{" +
                "id=" + id +
                ", subject=" + subject +
                ", relatedEntry=" + relatedEntry +
                ", issuedAt=" + issuedAt +
                ", status=" + status +
                ", reason='" + reason + '\'' +
                '}';
    }
}
