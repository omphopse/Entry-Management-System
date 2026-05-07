import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.*;

public class WriteDataToExcel {
    public static void writeNotificationToExcel(Notification n) {
        String filePath = AppConfig.DATA_FILE;
        try {
            File f = new File(filePath);
            Workbook workbook;

            if (f.exists()) {
                FileInputStream fis = new FileInputStream(f);
                workbook = new XSSFWorkbook(fis);
                fis.close();
            } else {
                workbook = new XSSFWorkbook();
            }

            while (workbook.getNumberOfSheets() <= 7) {
                workbook.createSheet("Sheet" + (workbook.getNumberOfSheets() + 1));
            }

            Sheet sheet = workbook.getSheetAt(7);

            Row header = sheet.getRow(0);
            if (header == null) {
                header = sheet.createRow(0);
                header.createCell(0).setCellValue("Id");
                header.createCell(1).setCellValue("RecipientVisitorId");
                header.createCell(2).setCellValue("RecipientUsername");
                header.createCell(3).setCellValue("Type");
                header.createCell(4).setCellValue("Subject");
                header.createCell(5).setCellValue("Message");
                header.createCell(6).setCellValue("CreatedAt");
                header.createCell(7).setCellValue("EmailSent");
                header.createCell(8).setCellValue("InAppShown");
            }

            int nextRowIdx = Math.max(1, sheet.getLastRowNum() + 1);
            Row row = sheet.createRow(nextRowIdx);

            row.createCell(0).setCellValue(n.getId());
            long rid = n.getRecipient() != null ? n.getRecipient().getId() : 0;
            row.createCell(1).setCellValue(rid);
            String ruser = n.getRecipient() != null && n.getRecipient().getUserAccount() != null
                    ? n.getRecipient().getUserAccount().getUsername()
                    : "";
            row.createCell(2).setCellValue(ruser);
            row.createCell(3).setCellValue(n.getType().toString());
            // generate subject and body according to NotificationType (keep simple here)
            String name = n.getRecipient() != null ? n.getRecipient().getFullName() : "User";
            String subject;
            
            switch (n.getType()) {
                case VISA_EXPIRY_WARNING:
                    subject = "Visa Expiry Warning for " + name;
                    break;
                case OVERSTAY_WARNING:
                    subject = "Overstay Warning for " + name;
                    break;
                case WARRANT_CREATED:
                    subject = "Warrant Issued for " + name;
                    break;
                default:
                    subject = "Notification for " + name;
            }

            String when = n.getCreatedAt() != null ? n.getCreatedAt().toString() : "now";
            String body;
            switch (n.getType()) {
                case VISA_EXPIRY_WARNING:
                    body = "Dear " + name + ",\n\nYour visa is about to expire. " + n.getMessage() + "\n\nTime: " + when + "\n\nRegards,\nBorder Control";
                    break;
                case OVERSTAY_WARNING:
                    body = "Dear " + name + ",\n\nOur records show you are overstaying. " + n.getMessage() + "\n\nTime: " + when + "\n\nRegards,\nBorder Control";
                    break;
                case WARRANT_CREATED:
                    body = "Dear " + name + ",\n\nA warrant has been created. " + n.getMessage() + "\n\nTime: " + when + "\n\nRegards,\nBorder Control";
                    break;
                default:
                    body = n.getMessage() != null ? n.getMessage() : "";
            }
            row.createCell(4).setCellValue(subject);
            row.createCell(5).setCellValue(body);
            row.createCell(6).setCellValue(n.getCreatedAt() != null ? n.getCreatedAt().toString() : "");
            row.createCell(7).setCellValue(n.isEmailSent());
            row.createCell(8).setCellValue(n.isInAppShown());

            FileOutputStream fos = new FileOutputStream(filePath);
            workbook.write(fos);
            fos.close();
            workbook.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void writeUserDataToExcel(User user) {
        String filePath = AppConfig.DATA_FILE;
        try {
            File f = new File(filePath);
            Workbook workbook;

            if (f.exists()) {
                FileInputStream fis = new FileInputStream(f);
                workbook = new XSSFWorkbook(fis);
                fis.close();
            } else {
                workbook = new XSSFWorkbook();
            }

            while (workbook.getNumberOfSheets() <= 1) {
                workbook.createSheet("Sheet" + (workbook.getNumberOfSheets() + 1));
            }

            Sheet sheet = workbook.getSheetAt(1);

            Row header = sheet.getRow(0);
            if (header == null) {
                header = sheet.createRow(0);
                header.createCell(0).setCellValue("Id");
                header.createCell(1).setCellValue("Username");
                header.createCell(2).setCellValue("Password");
                header.createCell(3).setCellValue("Role");
                header.createCell(4).setCellValue("Email");
            }

            int nextRowIdx = Math.max(1, sheet.getLastRowNum() + 1);

            Row row = sheet.createRow(nextRowIdx);
            row.createCell(0).setCellValue(user.getId());
            row.createCell(1).setCellValue(user.getUsername());
            row.createCell(2).setCellValue(user.getPassword());
            row.createCell(3).setCellValue(user.getRole().toString());
            row.createCell(4).setCellValue(user.getEmail());
            FileOutputStream fos = new FileOutputStream(filePath);
            workbook.write(fos);
            fos.close();
            workbook.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void writeVisitorDataToExcel(Visitor visitor) {
        String filePath = AppConfig.DATA_FILE;
        try {
            File f = new File(filePath);
            Workbook workbook;

            if (f.exists()) {
                FileInputStream fis = new FileInputStream(f);
                workbook = new XSSFWorkbook(fis);
                fis.close();
            } else {
                workbook = new XSSFWorkbook();
            }

            while (workbook.getNumberOfSheets() <= 2) {
                workbook.createSheet("Sheet" + (workbook.getNumberOfSheets() + 1));
            }

            Sheet sheet = workbook.getSheetAt(2);

            Row header = sheet.getRow(0);
            if (header == null) {
                header = sheet.createRow(0);
                header.createCell(0).setCellValue("Id");
                header.createCell(1).setCellValue("Username");
                header.createCell(2).setCellValue("FullName");
                header.createCell(3).setCellValue("PassportNumber");
                header.createCell(4).setCellValue("Nationality");
            }

            int nextRowIdx = Math.max(1, sheet.getLastRowNum() + 1);

            Row row = sheet.createRow(nextRowIdx);
            row.createCell(0).setCellValue(visitor.getId());
            row.createCell(1).setCellValue(visitor.getUserAccount().getUsername());
            row.createCell(2).setCellValue(visitor.getFullName());
            row.createCell(3).setCellValue(visitor.getPassportNumber());
            row.createCell(4).setCellValue(visitor.getNationality());

            FileOutputStream fos = new FileOutputStream(filePath);
            workbook.write(fos);
            fos.close();
            workbook.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void writeVisaApplicationDataToExcel(VisaApplication application) {
        String filePath = AppConfig.DATA_FILE;
        try {
            File f = new File(filePath);
            Workbook workbook;

            if (f.exists()) {
                FileInputStream fis = new FileInputStream(f);
                workbook = new XSSFWorkbook(fis);
                fis.close();
            } else {
                workbook = new XSSFWorkbook();
            }

            while (workbook.getNumberOfSheets() <= 3) {
                workbook.createSheet("Sheet" + (workbook.getNumberOfSheets() + 1));
            }

            Sheet sheet = workbook.getSheetAt(3);

            Row header = sheet.getRow(0);
            if (header == null) {
                header = sheet.createRow(0);
                header.createCell(0).setCellValue("Id");
                header.createCell(1).setCellValue("ApplicantId");
                header.createCell(2).setCellValue("ApplicantUsername");
                header.createCell(3).setCellValue("VisaType");
                header.createCell(4).setCellValue("ApplicationDate");
                header.createCell(5).setCellValue("Status");
                header.createCell(6).setCellValue("Reason");
                header.createCell(7).setCellValue("Rejection Reason");
                header.createCell(8).setCellValue("ProcessedById");
                header.createCell(9).setCellValue("VisaNumber");
            }

            int nextRowIdx = Math.max(1, sheet.getLastRowNum() + 1);

            Row row = sheet.createRow(nextRowIdx);
            row.createCell(0).setCellValue(application.getId());
            if (application.getApplicant() != null && application.getApplicant().getUserAccount() != null) {
                row.createCell(1).setCellValue(application.getApplicant().getUserAccount().getId());
                row.createCell(2).setCellValue(application.getApplicant().getUserAccount().getUsername());
            } else {
                row.createCell(1).setCellValue("");
                row.createCell(2).setCellValue("");
            }
            row.createCell(3).setCellValue(application.getVisaType().toString());
            row.createCell(4).setCellValue(application.getApplicationDate().toString());
            row.createCell(5).setCellValue(application.getStatus().toString());
            row.createCell(6).setCellValue(application.getReason() != null ? application.getReason() : "");
            row.createCell(7)
                    .setCellValue(application.getRejectionReason() != null ? application.getRejectionReason() : "");
            if (application.getProcessedBy() != null) {
                row.createCell(8).setCellValue(application.getProcessedBy().getId());
            } else {
                row.createCell(8).setCellValue("");
            }
            row.createCell(9).setCellValue("");

            FileOutputStream fos = new FileOutputStream(filePath);
            workbook.write(fos);
            fos.close();
            workbook.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void writeVisaDataToExcel(Visa visa) {
        String filePath = AppConfig.DATA_FILE;
        try {
            File f = new File(filePath);
            Workbook workbook;

            if (f.exists()) {
                FileInputStream fis = new FileInputStream(f);
                workbook = new XSSFWorkbook(fis);
                fis.close();
            } else {
                workbook = new XSSFWorkbook();
            }

            while (workbook.getNumberOfSheets() <= 4) {
                workbook.createSheet("Sheet" + (workbook.getNumberOfSheets() + 1));
            }

            Sheet sheet = workbook.getSheetAt(4);

            Row header = sheet.getRow(0);
            if (header == null) {
                header = sheet.createRow(0);
                header.createCell(0).setCellValue("Id");
                header.createCell(1).setCellValue("VisaNumber");
                header.createCell(2).setCellValue("HolderId");
                header.createCell(3).setCellValue("HolderUsername");
                header.createCell(4).setCellValue("VisaType");
                header.createCell(5).setCellValue("IssueDate");
                header.createCell(6).setCellValue("ExpiryDate");
                header.createCell(7).setCellValue("MaxStayDays");
                header.createCell(8).setCellValue("Status");
                header.createCell(9).setCellValue("SourceApplicationId");
            }

            int nextRowIdx = Math.max(1, sheet.getLastRowNum() + 1);

            Row row = sheet.createRow(nextRowIdx);
            row.createCell(0).setCellValue(visa.getId());
            row.createCell(1).setCellValue(visa.getVisaNumber());
            if (visa.getHolder() != null && visa.getHolder().getUserAccount() != null) {
                row.createCell(2).setCellValue(visa.getHolder().getUserAccount().getId());
                row.createCell(3).setCellValue(visa.getHolder().getUserAccount().getUsername());
            } else {
                row.createCell(2).setCellValue("");
                row.createCell(3).setCellValue("");
            }
            row.createCell(4).setCellValue(visa.getVisaType().toString());
            row.createCell(5).setCellValue(visa.getIssueDate() != null ? visa.getIssueDate().toString() : "");
            row.createCell(6).setCellValue(visa.getExpiryDate() != null ? visa.getExpiryDate().toString() : "");
            row.createCell(7).setCellValue(visa.getMaxStayDays());
            row.createCell(8).setCellValue(visa.getStatus() != null ? visa.getStatus().toString() : "");
            row.createCell(9).setCellValue(
                    visa.getSourceApplication() != null ? String.valueOf(visa.getSourceApplication().getId()) : "");

            FileOutputStream fos = new FileOutputStream(filePath);
            workbook.write(fos);
            fos.close();
            workbook.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void writeBorderEntryRecordDataToExcel(BorderEntryRecord record) {
        String filePath = AppConfig.DATA_FILE;
        try {
            File f = new File(filePath);
            Workbook workbook;

            if (f.exists()) {
                FileInputStream fis = new FileInputStream(f);
                workbook = new XSSFWorkbook(fis);
                fis.close();
            } else {
                workbook = new XSSFWorkbook();
            }

            while (workbook.getNumberOfSheets() <= 5) {
                workbook.createSheet("Sheet" + (workbook.getNumberOfSheets() + 1));
            }

            Sheet sheet = workbook.getSheetAt(5);

            Row header = sheet.getRow(0);
            if (header == null) {
                header = sheet.createRow(0);
                header.createCell(0).setCellValue("Id");
                header.createCell(1).setCellValue("VisitorId");
                header.createCell(2).setCellValue("VisitorUsername");
                header.createCell(3).setCellValue("VisaUsedId");
                header.createCell(4).setCellValue("EntryTime");
                header.createCell(5).setCellValue("EntryPoint");
                header.createCell(6).setCellValue("ExitTime");
                header.createCell(7).setCellValue("Status");
                header.createCell(8).setCellValue("ProcessedById");
            }

            int nextRowIdx = Math.max(1, sheet.getLastRowNum() + 1);

            Row row = sheet.createRow(nextRowIdx);
            row.createCell(0).setCellValue(record.getId());
            if (record.getVisitor() != null && record.getVisitor().getUserAccount() != null) {
                row.createCell(1).setCellValue(record.getVisitor().getUserAccount().getId());
                row.createCell(2).setCellValue(record.getVisitor().getUserAccount().getUsername());
            } else {
                row.createCell(1).setCellValue("");
                row.createCell(2).setCellValue("");
            }
            if (record.getVisaUsed() != null) {
                row.createCell(3).setCellValue(record.getVisaUsed().getId());
            } else {
                row.createCell(3).setCellValue("");
            }
            row.createCell(4).setCellValue(record.getEntryTime() != null ? record.getEntryTime().toString() : "");
            row.createCell(5).setCellValue(record.getEntryPoint() != null ? record.getEntryPoint() : "");
            row.createCell(6).setCellValue(record.getExitTime() != null ? record.getExitTime().toString() : "");
            row.createCell(7).setCellValue(record.getStatus() != null ? record.getStatus().toString() : "");
            if (record.getProcessedBy() != null) {
                row.createCell(8).setCellValue(record.getProcessedBy().getId());
            } else {
                row.createCell(8).setCellValue("");
            }

            FileOutputStream fos = new FileOutputStream(filePath);
            workbook.write(fos);
            fos.close();
            workbook.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void writeWarrantToExcel(Warrant w) {
        String filePath = AppConfig.DATA_FILE;
        try {
            File f = new File(filePath);
            Workbook workbook;

            if (f.exists()) {
                FileInputStream fis = new FileInputStream(f);
                workbook = new XSSFWorkbook(fis);
                fis.close();
            } else {
                workbook = new XSSFWorkbook();
            }

            while (workbook.getNumberOfSheets() <= 8) {
                workbook.createSheet("Sheet" + (workbook.getNumberOfSheets() + 1));
            }

            Sheet sheet = workbook.getSheetAt(8);

            Row header = sheet.getRow(0);
            if (header == null) {
                header = sheet.createRow(0);
                header.createCell(0).setCellValue("Id");
                header.createCell(1).setCellValue("SubjectVisitorId");
                header.createCell(2).setCellValue("SubjectUsername");
                header.createCell(3).setCellValue("RelatedEntryId");
                header.createCell(4).setCellValue("IssuedAt");
                header.createCell(5).setCellValue("Status");
                header.createCell(6).setCellValue("Reason");
            }

            int nextRowIdx = Math.max(1, sheet.getLastRowNum() + 1);
            Row row = sheet.createRow(nextRowIdx);

            row.createCell(0).setCellValue(w.getId());
            long sid = w.getSubject() != null ? w.getSubject().getId() : 0;
            row.createCell(1).setCellValue(sid);
            String suser = w.getSubject() != null && w.getSubject().getUserAccount() != null ? w.getSubject().getUserAccount().getUsername() : "";
            row.createCell(2).setCellValue(suser);
            long rel = w.getRelatedEntry() != null ? w.getRelatedEntry().getId() : 0;
            row.createCell(3).setCellValue(rel);
            row.createCell(4).setCellValue(w.getIssuedAt() != null ? w.getIssuedAt().toString() : "");
            row.createCell(5).setCellValue(w.getStatus() != null ? w.getStatus().toString() : "");
            row.createCell(6).setCellValue(w.getReason() != null ? w.getReason() : "");

            FileOutputStream fos = new FileOutputStream(filePath);
            workbook.write(fos);
            fos.close();
            workbook.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
