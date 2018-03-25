package jmr.px108;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/*
 * See
 * https://www.mkyong.com/java/javamail-api-sending-email-via-gmail-smtp-example/
 */

public class SendMailTLS_001 implements MailConstants {

	public static void main(String[] args) {

		final String username = EMAIL_FROM_ADDRESS;
		final String password = EMAIL_FROM_PASSWORD;

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", EMAIL_SERVER );
		props.put("mail.smtp.port", EMAIL_PORT );
		
		props.put("mail.smtp.socketFactory.port", EMAIL_PORT );
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory" );
		props.put("mail.smtp.socketFactory.fallback", "false" );
		SecurityManager security = System.getSecurityManager();

		
		
		Session session = Session.getInstance(props,
		  new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		  });

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress( EMAIL_FROM_ADDRESS ));
			message.setRecipients(Message.RecipientType.TO,
				InternetAddress.parse( EMAIL_TO_ADDRESS ));
			message.setSubject("test-subject");
			message.setText("test-body-text");

			System.out.println( "Calling: Transport.send().." );
			Transport.send(message);

			System.out.println("Done");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
}