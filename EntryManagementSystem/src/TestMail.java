import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;
import java.util.Scanner;

public class TestMail {

	public static void main(String[] args) {
		System.out.println("TestMail - send a test email via SMTP (Gmail)");
		System.out.println("Make sure you have added the Jakarta Mail JARs to the project's classpath (lib/)");
		System.out.println("For Gmail: create an App Password and use it.");

		try (Scanner scanner = new Scanner(System.in)) {
			System.out.print("Sender email (gmail): ");
			String senderEmail = scanner.nextLine().trim();

			System.out.print("Sender app password: ");
			String senderPassword = scanner.nextLine().trim();

			System.out.print("Recipient email: ");
			String recipientEmail = scanner.nextLine().trim();

			System.out.print("Subject: ");
			String subject = scanner.nextLine().trim();

			System.out.println("Body (single line). Press Enter when done:");
			String body = scanner.nextLine();

			// SMTP properties for Gmail
			Properties props = new Properties();
			props.put("mail.smtp.host", "smtp.gmail.com");
			props.put("mail.smtp.port", "587");
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.starttls.enable", "true");

			Session session = Session.getInstance(props, new Authenticator() {
				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(senderEmail, senderPassword);
				}
			});

			try {
				Message message = new MimeMessage(session);
				message.setFrom(new InternetAddress(senderEmail));
				message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
				message.setSubject(subject);
				message.setText(body);

				Transport.send(message);
				System.out.println("✅ Email sent successfully!");
			} catch (MessagingException e) {
				System.err.println("Failed to send email: " + e.getMessage());
				e.printStackTrace();
			}
		}
	}
}
