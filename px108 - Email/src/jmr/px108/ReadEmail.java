package jmr.px108;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;

public class ReadEmail implements MailConstants {

	public static void main(String[] args) throws Exception {
		
		final Properties p = new Properties();
		p.setProperty( "mail.store.protocol",  "imaps" );
		final Session session = Session.getDefaultInstance( p );
		final Store store = session.getStore( "imaps" );
		store.connect( "imap.gmail.com", EMAIL_FROM_ADDRESS, EMAIL_FROM_PASSWORD );
		
		final Folder folder = store.getFolder( "INBOX" );
		folder.open( Folder.READ_ONLY );
		
		final int count = folder.getMessageCount();
		System.out.println( "Message count: " + count );
//		final Message messages[] = folder.getMessages();
//		for ( final Message message : messages ) {
		for ( int i=count; i>count-4; i-- ) {
//			final Message message = messages[i];
			final Message message = folder.getMessage( i );
			
			final long lDate = message.getSentDate().getTime();
//			final ZoneOffset tz = ZoneOffset.of( "EST" );
			final ZoneOffset tz = ZoneOffset.UTC;
			final LocalDateTime ldt = LocalDateTime.ofEpochSecond( lDate, 0, tz ) ;
			
			System.out.println( "  Email " + i + ", "
						+ "size:" + message.getSize() + " bytes,  "
						+ "sent:" + ldt.toString() + ",  "
						+ "subject:" + message.getSubject() );
			final String strContent = message.getContent().toString();
			System.out.println( "      body: " + 
						strContent.substring( 0, Math.min( 60, strContent.length() ) ) );
		}
		
		folder.close();
		store.close();
	}
	
}
