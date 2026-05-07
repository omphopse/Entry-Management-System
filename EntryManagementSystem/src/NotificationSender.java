import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class NotificationSender {

    // Reads first ADMIN row from users sheet (sheet index 1) and returns [email, appPassword]
    private static String[] getAdminSenderCredentials() {
        String filePath = AppConfig.DATA_FILE;
        try {
            File f = new File(filePath);
            if (!f.exists()) return null;

            FileInputStream fis = new FileInputStream(f);
            Workbook workbook = new XSSFWorkbook(fis);

            if (workbook.getNumberOfSheets() <= 1) {
                workbook.close();
                fis.close();
                return null;
            }

            Sheet sheet = workbook.getSheetAt(1);
            DataFormatter formatter = new DataFormatter();

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue;
                String role = formatter.formatCellValue(row.getCell(3));
                if (role == null) continue;
                if ("ADMIN".equalsIgnoreCase(role.trim())) {
                    String adminEmail = formatter.formatCellValue(row.getCell(4));
                    String appPassword = formatter.formatCellValue(row.getCell(5));
                    workbook.close();
                    fis.close();
                    return new String[]{adminEmail != null ? adminEmail.trim() : "", appPassword != null ? appPassword.trim() : ""};
                }
            }

            workbook.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // fallback: allow sender credentials via environment variables
        try {
            String envEmail = System.getenv("BSF_SMTP_EMAIL");
            String envPass = System.getenv("BSF_SMTP_PASSWORD");
            if (envEmail != null && !envEmail.trim().isEmpty() && envPass != null && !envPass.trim().isEmpty()) {
                return new String[]{envEmail.trim(), envPass.trim()};
            }
        } catch (Exception ignored) {}
        return null;
    }

    private static String generateSubject(Notification n) {
        String name = n.getRecipient() != null ? n.getRecipient().getFullName() : "User";
        switch (n.getType()) {
            case VISA_EXPIRY_WARNING:
                return "Visa Expiry Warning for " + name;
            case OVERSTAY_WARNING:
                return "Overstay Warning for " + name;
            case WARRANT_CREATED:
                return "Warrant Issued for " + name;
            default:
                return "Notification for " + name;
        }
    }

    private static String generateBody(Notification n) {
        String name = n.getRecipient() != null ? n.getRecipient().getFullName() : "User";
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String when = n.getCreatedAt() != null ? n.getCreatedAt().format(fmt) : "now";

        switch (n.getType()) {
            case VISA_EXPIRY_WARNING:
                return "Dear " + name + ",\n\nYour visa is about to expire. " + n.getMessage() + "\n\nTime: " + when + "\n\nRegards,\nBorder Control";
            case OVERSTAY_WARNING:
                return "Dear " + name + ",\n\nOur records show you are overstaying. " + n.getMessage() + "\n\nTime: " + when + "\n\nRegards,\nBorder Control";
            case WARRANT_CREATED:
                return "Dear " + name + ",\n\nA warrant has been created. " + n.getMessage() + "\n\nTime: " + when + "\n\nRegards,\nBorder Control";
            default:
                return n.getMessage();
        }
    }

    private static boolean sendEmail(String senderEmail, String senderPassword, String recipientEmail, String subject, String body) {
        if (senderEmail == null || senderEmail.isEmpty() || senderPassword == null || senderPassword.isEmpty()) {
            System.out.println("Sender credentials are missing.");
            return false;
        }

        try {
            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");

            Session session = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(senderEmail, senderPassword);
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);
            return true;
        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static boolean sendNotification(Notification n) {
        if (n == null || n.getRecipient() == null || n.getRecipient().getUserAccount() == null) {
            System.out.println("Notification or recipient missing.");
            return false;
        }

        String recipientEmail = n.getRecipient().getUserAccount().getEmail();
        if (recipientEmail == null || recipientEmail.trim().isEmpty()) {
            System.out.println("Recipient has no email address.");
            return false;
        }

        String[] creds = getAdminSenderCredentials();
        if (creds == null) {
            System.out.println("No admin sender credentials found in users sheet.");
            return false;
        }

        String senderEmail = creds[0];
        String senderPassword = creds[1];

        String subject = generateSubject(n);
        String body = generateBody(n);

        boolean sent = sendEmail(senderEmail, senderPassword, recipientEmail, subject, body);
        n.setEmailSent(sent);

        // store notification in excel
        WriteDataToExcel.writeNotificationToExcel(n);

        return sent;
    }
}
