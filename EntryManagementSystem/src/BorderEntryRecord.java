import java.time.LocalDateTime;
enum BorderEntryStatus {
    IN_COUNTRY,
    EXITED,
    OVERSTAY
}
class BorderEntryRecord {
    private long id;
    private Visitor visitor;
    private Visa visaUsed;
    private LocalDateTime entryTime;
    private String entryPoint; 

    private LocalDateTime exitTime; 
    private BorderEntryStatus status; 

    private User processedBy; 

    public BorderEntryRecord(long id, Visitor visitor, Visa visaUsed, LocalDateTime entryTime, String entryPoint, LocalDateTime exitTime, BorderEntryStatus status, User processedBy) {
        this.id = id;
        this.visitor = visitor;
        this.visaUsed = visaUsed;
        this.entryTime = entryTime;
        this.entryPoint = entryPoint;
        this.exitTime = exitTime;
        this.status = status;
        this.processedBy = processedBy;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Visitor getVisitor() {
        return visitor;
    }

    public void setVisitor(Visitor visitor) {
        this.visitor = visitor;
    }

    public Visa getVisaUsed() {
        return visaUsed;
    }

    public void setVisaUsed(Visa visaUsed) {
        this.visaUsed = visaUsed;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(LocalDateTime entryTime) {
        this.entryTime = entryTime;
    }

    public String getEntryPoint() {
        return entryPoint;
    }

    public void setEntryPoint(String entryPoint) {
        this.entryPoint = entryPoint;
    }

    public LocalDateTime getExitTime() {
        return exitTime;
    }

    public void setExitTime(LocalDateTime exitTime) {
        this.exitTime = exitTime;
    }

    public BorderEntryStatus getStatus() {
        return status;
    }

    public void setStatus(BorderEntryStatus status) {
        this.status = status;
    }

    public User getProcessedBy() {
        return processedBy;
    }

    public void setProcessedBy(User processedBy) {
        this.processedBy = processedBy;
    }

    @Override
    public String toString() {
        return "BorderEntryRecord{" +
                "id=" + id +
                ", visitor=" + visitor +
                ", visaUsed=" + visaUsed +
                ", entryTime=" + entryTime +
                ", entryPoint='" + entryPoint + '\'' +
                ", exitTime=" + exitTime +
                ", status=" + status +
                ", processedBy=" + processedBy +
                '}';
    }
    
}
