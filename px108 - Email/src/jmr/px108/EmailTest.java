package jmr.px108;

// File Name SendEmail.java

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

/*
 * See:
 * https://www.tutorialspoint.com/java/java_sending_email.htm
 */


public class EmailTest implements MailConstants {

   public static void main(String [] args) {
	   
      // Recipient's email ID needs to be mentioned.
	      String to = EMAIL_TO_ADDRESS;

      // Sender's email ID needs to be mentioned
      String from = EMAIL_FROM_ADDRESS;

      // Assuming you are sending email from localhost
      String host = "localhost";

      // Get system properties
      Properties properties = System.getProperties();

      // Setup mail server
      properties.setProperty("mail.smtp.host", host);

      // Get the default Session object.
      Session session = Session.getDefaultInstance(properties);

      try {
         // Create a default MimeMessage object.
         MimeMessage message = new MimeMessage(session);

         // Set From: header field of the header.
         message.setFrom(new InternetAddress(from));

         // Set To: header field of the header.
         message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

         // Set Subject: header field
//         message.setSubject("This is the Subject Line!");
         message.setSubject("test-email-subject");

         // Now set the actual message
//         message.setText("This is actual message");
         message.setText("test-message-body");

         // Send message
         Transport.send(message);
         System.out.println("Sent message successfully....");
      } catch (MessagingException mex) {
         mex.printStackTrace();
      }
   }
	
}
