import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File;
import java.io.FileInputStream;

public class NotificationService {

    private final List<Visa> visas;
    private final List<BorderEntryRecord> entryRecords;
    private final List<Notification> notifications;
    private final List<Warrant> warrants;

    private Thread visaThread;
    private Thread overstayThread;
    private volatile boolean running = false;

    // keep in-memory keys to avoid duplicates during runtime:
    // type|recipientId|subject
    private final Set<String> sentKeys = new HashSet<>();

    public NotificationService(List<Visa> visas, List<BorderEntryRecord> entryRecords, List<Notification> notifications,
            List<Warrant> warrants) {
        this.visas = visas;
        this.entryRecords = entryRecords;
        this.notifications = notifications;
        this.warrants = warrants;
    }

    public void start() {
        if (running)
            return;
        running = true;

        visaThread = new Thread(this::visaExpiryLoop, "VisaExpiryChecker");
        visaThread.setDaemon(true);
        visaThread.start();

        overstayThread = new Thread(this::overstayLoop, "OverstayChecker");
        overstayThread.setDaemon(true);
        overstayThread.start();
    }

    public void stop() {
        running = false;
        if (visaThread != null)
            visaThread.interrupt();
        if (overstayThread != null)
            overstayThread.interrupt();
    }

    private void visaExpiryLoop() {
        while (running) {
            try {
                checkVisaExpiry();
                Thread.sleep(60 * 1000); // check every 60 seconds
            } catch (InterruptedException ie) {
                break;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void overstayLoop() {
        while (running) {
            try {
                checkOverstays();
                Thread.sleep(15 * 1000); // check every 60 seconds
            } catch (InterruptedException ie) {
                break;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void checkVisaExpiry() {
        synchronized (visas) {
            for (Visa v : visas) {
                try {
                    if (v == null)
                        continue;
                    if (v.getStatus() == VisaStatus.ACTIVE && v.getExpiryDate() != null) {
                        long daysLeft = ChronoUnit.DAYS.between(LocalDate.now(), v.getExpiryDate());
                        if (daysLeft <= 2 && daysLeft >= 0) {
                            Visitor recip = v.getHolder();
                            if (recip != null) {
                                String subject = "Visa Expiry Warning for " + recip.getFullName();
                                String key = NotificationKey(NotificationType.VISA_EXPIRY_WARNING, recip, subject);
                                if (isAlreadyNotified(key))
                                    continue;

                                // Use NotificationManager for consistency and best practices
                                NotificationManager.notifyVisaExpiryWarning(recip, v, notifications);
                                markNotified(key);
                            }
                        }
                    }
                } catch (Exception e) {
                    // ignore per-row errors
                }
            }
        }
    }

    private void checkOverstays() {
        synchronized (entryRecords) {
            for (BorderEntryRecord r : entryRecords) {
                try {
                    if (r == null)
                        continue;
                    if (r.getStatus() == BorderEntryStatus.IN_COUNTRY) {
                        Visitor recip = r.getVisitor();
                        Visa used = r.getVisaUsed();
                        boolean overstay = false;
                        int daysOverdue = 0;

                        if (used != null) {
                            if (used.getExpiryDate() != null && used.getExpiryDate().isBefore(LocalDate.now())) {
                                overstay = true;
                                daysOverdue = (int) ChronoUnit.DAYS.between(used.getExpiryDate(), LocalDate.now());
                            } else if (r.getEntryTime() != null) {
                                long stayed = ChronoUnit.DAYS.between(r.getEntryTime().toLocalDate(), LocalDate.now());
                                if (used != null && used.getMaxStayDays() > 0 && stayed > used.getMaxStayDays()) {
                                    overstay = true;
                                    daysOverdue = (int) (stayed - used.getMaxStayDays());
                                }
                            }
                        }

                        if (overstay && recip != null) {
                            // Send overstay warning notification
                            String subject = "Overstay Warning for " + recip.getFullName();
                            String key = NotificationKey(NotificationType.OVERSTAY_WARNING, recip, subject);
                            if (!isAlreadyNotified(key)) {
                                NotificationManager.notifyOverstayWarning(recip, r, used, daysOverdue, notifications);
                                markNotified(key);
                            }

                            // Create warrant if overstayed > 2 days
                            if (daysOverdue > 2 && recip != null) {
                                // Mark border entry record as OVERSTAY
                                try {
                                    if (r.getStatus() != BorderEntryStatus.OVERSTAY) {
                                        r.setStatus(BorderEntryStatus.OVERSTAY);
                                        UpdateExcelData.updateCellInExcel(5, r.getId(), 0, 7, "OVERSTAY");
                                    }
                                } catch (Exception sx) {
                                    // best-effort persistence, continue
                                }

                                String wsub = "Warrant Issued for " + recip.getFullName();
                                String wkey = NotificationKey(NotificationType.WARRANT_CREATED, recip, wsub);
                                if (!isAlreadyNotified(wkey)) {
                                    try {
                                        // Create Warrant and use NotificationManager
                                        long wid;
                                        synchronized (warrants) {
                                            wid = warrants.size() + 1;
                                            Warrant warr = new Warrant(wid, recip, r, LocalDateTime.now(),
                                                    WarrantStatus.ACTIVE,
                                                    "Overstay of " + daysOverdue + " days");
                                            warrants.add(warr);
                                            
                                            // Use NotificationManager for warrant creation notification
                                            NotificationManager.notifyWarrantCreated(recip, warr, notifications);
                                        }
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }
                                    markNotified(wkey);
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    // skip malformed record
                }
            }
        }
    }

    private String NotificationKey(NotificationType type, Visitor recip, String subject) {
        long rid = recip != null ? recip.getId() : 0L;
        return type.toString() + "|" + rid + "|" + subject;
    }

    private synchronized boolean isAlreadyNotified(String key) {
        if (sentKeys.contains(key))
            return true;
        // also check sheet to avoid duplicates across runs
        try {
            File f = new File(AppConfig.DATA_FILE);
            if (!f.exists())
                return false;
            FileInputStream fis = new FileInputStream(f);
            Workbook workbook = new XSSFWorkbook(fis);
            if (workbook.getNumberOfSheets() <= 7) {
                workbook.close();
                fis.close();
                return false;
            }
            Sheet sheet = workbook.getSheetAt(7);
            DataFormatter fmt = new DataFormatter();
            for (Row row : sheet) {
                if (row.getRowNum() == 0)
                    continue;
                String t = fmt.formatCellValue(row.getCell(3));
                String subj = fmt.formatCellValue(row.getCell(4));
                String ridStr = fmt.formatCellValue(row.getCell(1));
                long rid = 0;
                try {
                    rid = Long.parseLong(ridStr.trim());
                } catch (Exception ex) {
                }
                if (t != null && subj != null && t.equalsIgnoreCase(typeFromKey(key)) && subj.equals(subjFromKey(key))
                        && rid == idFromKey(key)) {
                    workbook.close();
                    fis.close();
                    sentKeys.add(key);
                    return true;
                }
            }
            workbook.close();
            fis.close();
        } catch (Exception e) {
            // ignore
        }
        return false;
    }

    private synchronized void markNotified(String key) {
        sentKeys.add(key);
    }

    // helpers to parse key
    private String typeFromKey(String key) {
        if (key == null)
            return "";
        String[] parts = key.split("\\|", 3);
        return parts.length > 0 ? parts[0] : "";
    }

    private long idFromKey(String key) {
        if (key == null)
            return 0;
        String[] parts = key.split("\\|", 3);
        if (parts.length > 1) {
            try {
                return Long.parseLong(parts[1]);
            } catch (Exception e) {
            }
        }
        return 0;
    }

    private String subjFromKey(String key) {
        if (key == null)
            return "";
        String[] parts = key.split("\\|", 3);
        return parts.length > 2 ? parts[2] : "";
    }
}
