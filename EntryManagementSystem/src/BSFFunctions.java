import java.util.List;

public class BSFFunctions {

    public static void writeUserDataToExcel(User user) {
        WriteDataToExcel.writeUserDataToExcel(user);
    }

    public static void writeVisitorDataToExcel(Visitor visitor) {
        WriteDataToExcel.writeVisitorDataToExcel(visitor);
    }

    public static void writeVisaApplicationDataToExcel(VisaApplication app) {
        WriteDataToExcel.writeVisaApplicationDataToExcel(app);
    }

    public static void writeVisaDataToExcel(Visa visa) {
        WriteDataToExcel.writeVisaDataToExcel(visa);
    }

    public static void writeBorderEntryRecordDataToExcel(BorderEntryRecord rec) {
        WriteDataToExcel.writeBorderEntryRecordDataToExcel(rec);
    }

    public static void updateCellInExcel(int sheetIndex, long recordId, int idColumnIndex, int targetCellIndex,String cellValue) {
        UpdateExcelData.updateCellInExcel(sheetIndex, recordId, idColumnIndex, targetCellIndex, cellValue);
    }

    public static void updateVisaApplicationCellInExcel(long applicationId, int cellIndex, String cellValue) {
        UpdateExcelData.updateVisaApplicationCellInExcel(applicationId, cellIndex, cellValue);
    }

    public static void displayApplication(User user) {
        DisplayExcelData.displayApplication(user);
    }

    public static void displayVisasForUser(User user) {
        DisplayExcelData.displayVisasForUser(user);
    }

    public static void displayAllVisas() {
        DisplayExcelData.displayAllVisas();
    }

    public static void loadVisitorsFromExcel(List<Visitor> visitors, List<User> users) {
        LoadExcelData.loadVisitorsFromExcel(visitors, users);
    }

    public static void loadVisaApplicationsFromExcel(List<VisaApplication> applications, List<Visitor> visitors) {
        LoadExcelData.loadVisaApplicationsFromExcel(applications, visitors);
    }

    public static void loadVisasFromExcel(List<Visa> visas, List<Visitor> visitors,
            List<VisaApplication> applications) {
        LoadExcelData.loadVisasFromExcel(visas, visitors, applications);
    }

    public static void loadBorderEntryRecordsFromExcel(List<BorderEntryRecord> records, List<Visitor> visitors,
            List<Visa> visas) {
        LoadExcelData.loadBorderEntryRecordsFromExcel(records, visitors, visas);
    }

    public static void loadUsersFromExcel(List<User> users) {
        LoadExcelData.loadUsersFromExcel(users);
    }

    public static void displayBorderEntryRecords(User user) {
        DisplayExcelData.displayBorderEntryRecords(user);
    }

    public static void displayBorderHistoryForApplicant(User user, List<BorderEntryRecord> entryRecords) {
        DisplayExcelData.displayBorderHistoryForApplicant(user, entryRecords);
    }

    public static boolean sendNotification(Notification n) {
        return NotificationSender.sendNotification(n);
    }

    public static void writeNotificationToExcel(Notification n) {
        WriteDataToExcel.writeNotificationToExcel(n);
    }

    public static void writeWarrantToExcel(Warrant w) {
        WriteDataToExcel.writeWarrantToExcel(w);
    }

    public static void displayNotificationsForUser(User user, java.util.List<Notification> notifications) {
        DisplayExcelData.displayNotificationsForUser(user, notifications);
    }

    public static void displayAllNotifications(java.util.List<Notification> notifications) {
        DisplayExcelData.displayAllNotifications(notifications);
    }

    public static void displayAllWarrants(java.util.List<Warrant> warrants) {
        DisplayExcelData.displayAllWarrants(warrants);
    }

    public static void displayAllUsers(java.util.List<User> users) {
        DisplayExcelData.displayAllUsers(users);
    }

    public static void displayWarrantsForUser(User user, java.util.List<Warrant> warrants) {
        DisplayExcelData.displayWarrantsForUser(user, warrants);
    }

    public static void displayAllVisitors(java.util.List<Visitor> visitors) {
        DisplayExcelData.displayAllVisitors(visitors);
    }

    public static void displayAllUsersForAdmin(java.util.List<User> users) {
        DisplayExcelData.displayAllUsersForAdmin(users);
    }
}
