import java.util.List;
import java.util.ArrayList;

class Visitor {
    private long id;
    private User userAccount; 
    private String fullName;
    private String passportNumber;
    private String nationality;

    private List<Visa> visas;
    private List<BorderEntryRecord> entryHistory;

    public Visitor(User userAccount, String passportNumber, String nationality) {
        this.id = userAccount.getId();
        this.fullName = userAccount.getUsername();
        this.userAccount = userAccount;
        this.passportNumber = passportNumber;   
        this.nationality = nationality;
        this.visas = new ArrayList<>();
        this.entryHistory = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUserAccount() {
        return userAccount;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassportNumber() {
        return passportNumber;
    }
    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public String getNationality() {
        return nationality;
    }
    public void setNationality(String nationality) {
        this.nationality = nationality; 
    }

    public List<Visa> getVisas() {
        return visas;
    }

    public List<BorderEntryRecord> getEntryHistory() {
        return entryHistory;
    }

    public void addVisa(Visa visa) {
        visas.add(visa);
    }

    public void addEntryRecord(BorderEntryRecord record) {
        entryHistory.add(record);
    }

    public boolean hasVisa(Visa visa) {
        return visas.contains(visa);
    }

    public boolean hasEntryRecord(BorderEntryRecord record) {
        return entryHistory.contains(record);
    }

    public String toString() {
        return "Visitor{" +
                "id=" + id +
                ", userAccount=" + userAccount +
                ", fullName='" + fullName + '\'' +
                ", passportNumber='" + passportNumber + '\'' +
                ", nationality='" + nationality + '\'' +
                ", visas=" + visas +
                ", entryHistory=" + entryHistory +
                '}';
    }

}
