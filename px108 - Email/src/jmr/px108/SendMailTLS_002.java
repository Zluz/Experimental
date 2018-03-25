package jmr.px108;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/*
 * See
 * https://www.youtube.com/watch?v=F51dZmaMCQc
 */

public class SendMailTLS_002 implements MailConstants {

	public static class SMTPAuthenticator extends javax.mail.Authenticator {
		public PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication( 
								EMAIL_FROM_ADDRESS, EMAIL_FROM_PASSWORD );
		}
	}
	
	
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

		try {
			Authenticator auth = new SMTPAuthenticator();
			Session session = Session.getInstance( props, auth );
			
			MimeMessage msg = new MimeMessage( session );
			msg.setText( "test-body" );
			msg.setSubject( "test-subject" );
			msg.setFrom( new InternetAddress( EMAIL_FROM_ADDRESS ) );
			msg.addRecipient( Message.RecipientType.TO, 
								new InternetAddress( EMAIL_TO_ADDRESS ) );
			
			System.out.println( "Sending message.." );
			Transport.send( msg );
			System.out.println( "Message sent." );
		} catch ( final Exception e ) {
			e.printStackTrace();
		}
		

	}
}