import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.*;
import java.time.LocalDate;
import java.util.List;

public class LoadExcelData {

    public static void loadUsersFromExcel(List<User> users) {
        try {
            FileInputStream fis = new FileInputStream(AppConfig.DATA_FILE);
            Workbook workbook = new XSSFWorkbook(fis);
            Sheet sheet = workbook.getSheetAt(1);
            DataFormatter formatter = new DataFormatter();

            for (Row row : sheet) {
                if (row.getRowNum() == 0)continue;
                String id = formatter.formatCellValue(row.getCell(0));
                String username = formatter.formatCellValue(row.getCell(1));
                String password = formatter.formatCellValue(row.getCell(2));
                String roleStr = formatter.formatCellValue(row.getCell(3));
                String email = formatter.formatCellValue(row.getCell(4));
                UserRole role;
                switch (roleStr.toUpperCase()) {
                    case "APPLICANT":
                        role = UserRole.APPLICANT;
                        break;
                    case "ADMIN":
                        role = UserRole.ADMIN;
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid role: " + roleStr);
                }

                boolean userExists = false;
                for (User u : users) {
                    if (u.getUsername().equalsIgnoreCase(username)) {
                        userExists = true;
                        break;
                    }
                }

                if (!userExists) {
                    User user = new User(username, password, role, email);
                    users.add(user);
                }
            }

            workbook.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadVisitorsFromExcel(List<Visitor> visitors, List<User> users) {
        String filePath = AppConfig.DATA_FILE;
        try {
            File f = new File(filePath);
            if (!f.exists())
                return;

            FileInputStream fis = new FileInputStream(f);
            Workbook workbook = new XSSFWorkbook(fis);

            if (workbook.getNumberOfSheets() <= 2) {
                workbook.close();
                fis.close();
                return;
            }

            Sheet sheet = workbook.getSheetAt(2);
            DataFormatter formatter = new DataFormatter();

            for (Row row : sheet) {
                if (row.getRowNum() == 0)
                    continue;

                String idStr = formatter.formatCellValue(row.getCell(0));
                if (idStr == null || idStr.trim().isEmpty())
                    continue;

                try {
                    long visitorId = Long.parseLong(idStr.trim());
                    String username = formatter.formatCellValue(row.getCell(1));
                    String fullName = formatter.formatCellValue(row.getCell(2));
                    String passport = formatter.formatCellValue(row.getCell(3));
                    String nationality = formatter.formatCellValue(row.getCell(4));

                    // Find the corresponding user
                    User userAccount = null;
                    for (User u : users) {
                        if (u.getUsername().equals(username)) {
                            userAccount = u;
                            break;
                        }
                    }

                    if (userAccount != null) {
                        Visitor visitor = new Visitor(userAccount, passport, nationality);
                        visitor.setId(visitorId);
                        if (fullName != null && !fullName.isEmpty()) {
                            visitor.setFullName(fullName);
                        }
                        visitors.add(visitor);
                    }
                } catch (Exception e) {

                }
            }

            workbook.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadVisaApplicationsFromExcel(List<VisaApplication> applications, List<Visitor> visitors) {
        String filePath = AppConfig.DATA_FILE;
        try {
            File f = new File(filePath);
            if (!f.exists())
                return;

            FileInputStream fis = new FileInputStream(f);
            Workbook workbook = new XSSFWorkbook(fis);

            if (workbook.getNumberOfSheets() <= 3) {
                workbook.close();
                fis.close();
                return;
            }

            Sheet sheet = workbook.getSheetAt(3);
            DataFormatter formatter = new DataFormatter();

            for (Row row : sheet) {
                if (row.getRowNum() == 0)
                    continue;

                String idStr = formatter.formatCellValue(row.getCell(0));
                if (idStr == null || idStr.trim().isEmpty())
                    continue;

                try {
                    long appId = Long.parseLong(idStr.trim());
                    long applicantId = Long.parseLong(formatter.formatCellValue(row.getCell(1)));
                    String applicantUsername = formatter.formatCellValue(row.getCell(2));
                    String visaTypeStr = formatter.formatCellValue(row.getCell(3));
                    String appDateStr = formatter.formatCellValue(row.getCell(4));
                    String statusStr = formatter.formatCellValue(row.getCell(5));
                    String reason = formatter.formatCellValue(row.getCell(6));
                    String rejectionReason = formatter.formatCellValue(row.getCell(7));
                    String processedByIdStr = formatter.formatCellValue(row.getCell(8));

                    VisaType visaType = VisaType.valueOf(visaTypeStr);
                    ApplicationStatus status = ApplicationStatus.valueOf(statusStr);
                    LocalDate appDate = LocalDate.parse(appDateStr);

                    Visitor applicant = null;
                    for (Visitor v : visitors) {
                        if (v.getUserAccount().getId() == applicantId) {
                            applicant = v;
                            break;
                        }
                    }

                    VisaApplication app = new VisaApplication(applicant, visaType, appDate, status, reason,
                            rejectionReason, null);
                    app.setId(appId);
                    applications.add(app);
                } catch (Exception e) {
                    // skip malformed rows
                }
            }

            workbook.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadVisasFromExcel(List<Visa> visas, List<Visitor> visitors,
            List<VisaApplication> applications) {
        String filePath = AppConfig.DATA_FILE;
        try {
            File f = new File(filePath);
            if (!f.exists())
                return;

            FileInputStream fis = new FileInputStream(f);
            Workbook workbook = new XSSFWorkbook(fis);

            if (workbook.getNumberOfSheets() <= 4) {
                workbook.close();
                fis.close();
                return;
            }

            Sheet sheet = workbook.getSheetAt(4);
            DataFormatter formatter = new DataFormatter();

            for (Row row : sheet) {
                if (row.getRowNum() == 0)
                    continue;

                String idStr = formatter.formatCellValue(row.getCell(0));
                if (idStr == null || idStr.trim().isEmpty())
                    continue;

                try {
                    long visaId = Long.parseLong(idStr.trim());
                    String visaNumber = formatter.formatCellValue(row.getCell(1));
                    long holderId = Long.parseLong(formatter.formatCellValue(row.getCell(2)));
                    String visaTypeStr = formatter.formatCellValue(row.getCell(4));
                    String issueDateStr = formatter.formatCellValue(row.getCell(5));
                    String expiryDateStr = formatter.formatCellValue(row.getCell(6));
                    int maxStayDays = (int) Double.parseDouble(formatter.formatCellValue(row.getCell(7)));
                    String statusStr = formatter.formatCellValue(row.getCell(8));
                    String sourceAppIdStr = formatter.formatCellValue(row.getCell(9));

                    VisaType visaType = VisaType.valueOf(visaTypeStr);
                    VisaStatus status = VisaStatus.valueOf(statusStr);
                    LocalDate issueDate = LocalDate.parse(issueDateStr);
                    LocalDate expiryDate = LocalDate.parse(expiryDateStr);

                    Visitor holder = null;
                    for (Visitor v : visitors) {
                        if (v.getUserAccount().getId() == holderId) {
                            holder = v;
                            break;
                        }
                    }

                    VisaApplication sourceApp = null;
                    if (!sourceAppIdStr.isEmpty()) {
                        long sourceAppId = Long.parseLong(sourceAppIdStr.trim());
                        for (VisaApplication a : applications) {
                            if (a.getId() == sourceAppId) {
                                sourceApp = a;
                                break;
                            }
                        }
                    }

                    Visa visa = new Visa(visaId, visaNumber, holder, visaType, issueDate, expiryDate, maxStayDays,
                            status, sourceApp);
                    visas.add(visa);
                } catch (Exception e) {
                    // skip malformed rows
                }
            }

            workbook.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadBorderEntryRecordsFromExcel(List<BorderEntryRecord> records, List<Visitor> visitors,
            List<Visa> visas) {
        String filePath = AppConfig.DATA_FILE;
        try {
            File f = new File(filePath);
            if (!f.exists())
                return;

            FileInputStream fis = new FileInputStream(f);
            Workbook workbook = new XSSFWorkbook(fis);

            if (workbook.getNumberOfSheets() <= 5) {
                workbook.close();
                fis.close();
                return;
            }

            Sheet sheet = workbook.getSheetAt(5);
            DataFormatter formatter = new DataFormatter();

            for (Row row : sheet) {
                if (row.getRowNum() == 0)
                    continue;

                String idStr = formatter.formatCellValue(row.getCell(0));
                if (idStr == null || idStr.trim().isEmpty())
                    continue;

                try {
                    long recordId = Long.parseLong(idStr.trim());
                    long visitorId = Long.parseLong(formatter.formatCellValue(row.getCell(1)));
                    String visaUsedIdStr = formatter.formatCellValue(row.getCell(3));
                    String entryTimeStr = formatter.formatCellValue(row.getCell(4));
                    String entryPoint = formatter.formatCellValue(row.getCell(5));
                    String exitTimeStr = formatter.formatCellValue(row.getCell(6));
                    String statusStr = formatter.formatCellValue(row.getCell(7));

                    Visitor visitor = null;
                    for (Visitor v : visitors) {
                        if (v.getUserAccount().getId() == visitorId) {
                            visitor = v;
                            break;
                        }
                    }

                    Visa visaUsed = null;
                    if (!visaUsedIdStr.isEmpty()) {
                        long visaUsedId = Long.parseLong(visaUsedIdStr.trim());
                        for (Visa v : visas) {
                            if (v.getId() == visaUsedId) {
                                visaUsed = v;
                                break;
                            }
                        }
                    }

                    java.time.LocalDateTime entryTime = entryTimeStr.isEmpty() ? null
                            : java.time.LocalDateTime.parse(entryTimeStr);
                    java.time.LocalDateTime exitTime = exitTimeStr.isEmpty() ? null
                            : java.time.LocalDateTime.parse(exitTimeStr);
                    BorderEntryStatus status = BorderEntryStatus.valueOf(statusStr);

                    BorderEntryRecord record = new BorderEntryRecord(recordId, visitor, visaUsed, entryTime, entryPoint,
                            exitTime, status, null);
                    records.add(record);
                } catch (Exception e) {

                }
            }

            workbook.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
