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

import javax.mail.*;
import javax.mail.internet.*;
import java.util.*;

// https://www.geeksforgeeks.org/sending-email-java-ssltls-authentication/

public class SendMailTLS_004 implements MailConstants {
//public class SendEmail {
	
//	final static String emailSMTPserver = "smtp.gmail.com";
//	final String emailServerPort = "465";
//	String receiverEmailID = null;
//	static String emailSubject = "Test Mail";
//	static String emailBody = ":)";

//	public void SendEmail(String receiverEmailID, String Subject, String Body) {
	public static void main( final String args[] ) {
		
//		final String receiverEmailID = receiverEmailID;
//		final String emailSubject = "test-subject";
//		final String emailBody = "test-body";
		
//		Properties props = new Properties();
		Properties props = System.getProperties();
		
		props.put("mail.smtp.user", EMAIL_FROM_ADDRESS );
		props.put("mail.smtp.host", EMAIL_SERVER );
		props.put("mail.smtp.port", EMAIL_PORT );
		
//		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.auth", "localhost" );
//		props.put("mail.smtp.auth", EMAIL_SERVER );
		props.put("mail.smtp.starttls.enable", "true");
//		props.put("mail.smtp.starttls.required", "true");
		
		props.put("mail.smtp.socketFactory.port", EMAIL_PORT );
		props.put("mail.smtp.socketFactory.fallback", "false");
		
		props.put("mail.smtp.socketFactory.class",
	                		"javax.net.ssl.SSLSocketFactory");
	      
//		java.security.Security.addProvider( 
//						new com.sun.net.ssl.internal.ssl.Provider() );
//		Session mailSession = Session.getDefaultInstance( props, null );
//		mailSession.setDebug( false );
		
		
		
//		SecurityManager security = System.getSecurityManager();
		try {
//			Authenticator auth = new SMTPAuthenticator();
			Session session = Session.getDefaultInstance( 
								props, new SMTPAuthenticator() );
			
			Message msg = new MimeMessage(session);
			msg.setText( MESSAGE_BODY );
			msg.setSubject( MESSAGE_SUBJECT );
			msg.setFrom( new InternetAddress( EMAIL_FROM_ADDRESS ) );
			final InternetAddress[] addresses = 
							{ new InternetAddress( EMAIL_TO_ADDRESS ) };
//			msg.addRecipient(Message.RecipientType.TO,
//					new InternetAddress( EMAIL_TO_ADDRESS ));
			msg.setRecipients( Message.RecipientType.TO, addresses );
			msg.setSentDate( new Date() );
			
//			Transport transport = session.getTransport( "smtp" );
//			System.out.println( "Connecting.." );
//			transport.connect( EMAIL_SERVER, 
//							EMAIL_FROM_ADDRESS, EMAIL_FROM_PASSWORD );
//			
//			System.out.println( "Sending.." );
//			transport.sendMessage( msg, msg.getAllRecipients() );
//			transport.close();

			System.out.println( "Sending.." );
			Transport.send( msg );

			
			System.out.println("Message send Successfully:)");
		} catch ( Exception e ) {
			System.err.println( "Exception occurred: " + e.toString() );
			e.printStackTrace();
		}
	}

	public static class SMTPAuthenticator extends javax.mail.Authenticator {
		public PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(
								EMAIL_FROM_ADDRESS, EMAIL_FROM_PASSWORD );
		}
	}
	
	
}