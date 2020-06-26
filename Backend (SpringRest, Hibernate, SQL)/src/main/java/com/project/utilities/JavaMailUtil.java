package com.project.utilities;

import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.InternetHeaders;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.project.model.Booking;


public class JavaMailUtil {
	

	public static String email = "";								//ADD YOUR EMAIL(GMAIL ACCOUNT) YOU WISH TO SENT MAILS FROM
	public static String password = "";								//ADD THE PASSWORD OF THE EMAIL PROVIDED 
    
	
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//					SENDS TICKET MAIL ON BOOKING SUCCESSFUL
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------	

	public static Integer cid=0;
	
	public static void generateCid() {
		cid++;
	}
	
	public static void sendMail(String toEmail,Booking booking) throws Exception {
				  
	      Properties properties = System.getProperties();

	      properties.put("mail.smtp.auth", "true");
	      properties.put("mail.smtp.starttls.enable", "true");
	      properties.put("mail.smtp.host", "smtp.gmail.com");
	      properties.put("mail.smtp.port", "587");
	      properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");
		
	      
	      Session session = Session.getInstance(properties,new Authenticator() {
	    	  @Override
	    	protected PasswordAuthentication getPasswordAuthentication() {
	    				return new PasswordAuthentication(email,password);
	    	}
		});
	      
	      Message message=prepareMessage(session,toEmail,booking);
	      Transport.send(message);
	}
	
	
	private static Message prepareMessage(Session session,String toEmail,Booking booking) {
		
		Message message=new MimeMessage(session);
		try {
			String str=" Booking ID: "+booking.getBookingId()+"\n Booking on Name: "+booking.getFullName()+"\n Email: "+booking.getEmail()+
					"\n No of Bookings: "+booking.getNoOfPeople()+"\n Event Date: "+booking.getEvent().getDateOfEvent()+"\n Event Name: "+
					booking.getEvent().getEventName();
			
			byte[] image=QRCodeGeneration.code(str);
			
			generateCid();
			
			message.setFrom(new InternetAddress(email));
			message.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
			message.setSubject("Booking details Update");
				
			
		    MimeMultipart multipart = new MimeMultipart("related");
		    MimeBodyPart  messageBodyPart = new MimeBodyPart();
	        String htmlText = "<HTML>\r\n" + 
	        		"<body >\r\n" + 
	        		"<H1 style=\"color:#023F1D\"> 	<center>	<i>BOOKING SUCCESSFUL</i></center></H1>\r\n" + 
	        		"<hr style=\"height:2px;border-width:0;color:gray;background-color: #88D8AB\">"+
	        		"<img src=\"cid:"+cid+"\"  style=\"float: right;  margin-right: 10%\"/>"+
	        		"<h3 style=\" margin-left: 10%\" >BOOKING ID: "+ booking.getBookingId() +"</H3>\r\n" + 
	        				"<H3 style=\" margin-left: 10%\">BOOKED ON NAME: "+ booking.getFullName() +"</H3>\r\n" + 
	        				"<H3 style=\" margin-left: 10%\">EVENT BOOKED : "+ booking.getEvent().getEventName() +"</H3>\r\n" + 
	        				"<H3 style=\" margin-left: 10%\">EVENT DATE: "+ booking.getEvent().getDateOfEvent() +"</H3>\r\n" + 
	        				"<H3 style=\" margin-left: 10%\">NO OF TICKETS BOOKED : "+ booking.getNoOfPeople() +"</H3>\r\n" + 
	        				"<hr style=\"height:2px;border-width:0;color:gray;background-color: #88D8AB\">\r\n" + 
	        				"<ul>\r\n" + 
	        				"<li><H5 ><i>Please have your ID proof handy with you while attending the event</i></H5></li>\r\n" + 
	        				"<li><h5 ><i>Please reach the venue or get connected for online session 15 mins prior the schedule time</i></h5></li>\r\n" + 
	        				"<li><h5 ><i>For a live event carrying id proof and having this mail with you in your phone is mandatory</i></h5></li>\r\n" + 
	        				"<li><h5 ><i>Screenshot of mail will not be accepted we suggest you to forward the mail directly to the person attending the event</i></h5></li>\r\n" + 
	        				"<li><h5 ><i>Please forward the mail from the same email Id to which this mail was received in your absence the mail Id will help us in the authentication</i> </h5></li>\r\n" + 
	        				"</ul>\r\n" + "<br><h5 style=\"text-align:center;\"><<<i>This is a system generated mail</i>>></h5>"+
	        				"</body>\r\n" + 
	        				"</HTML>";

	        messageBodyPart.setText(htmlText,"US-ASCII", "html");
	        multipart.addBodyPart(messageBodyPart);
	        
	        InternetHeaders headers = new InternetHeaders();
	        headers.addHeader("Content-Type", "image/jpg");
	        headers.addHeader("Content-Transfer-Encoding", "base64");
	        MimeBodyPart messageBodyPart2 = new MimeBodyPart(headers,image);
	       
	        messageBodyPart2.setDisposition(MimeBodyPart.INLINE);
            messageBodyPart2.setFileName("QRCode");
			messageBodyPart2.setContentID("<"+cid+">");
			
			multipart.addBodyPart(messageBodyPart2);
		   
		    message.setContent(multipart);  
		    
			return message;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//			SENDS MAIL ON ADMIN CREATION
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------	
	public static void sendMailAdmin(String toEmail,long adminId,String msgPassword) throws Exception {
		  
	      Properties properties = System.getProperties();

	      properties.put("mail.smtp.auth", "true");
	      properties.put("mail.smtp.starttls.enable", "true");
	      properties.put("mail.smtp.host", "smtp.gmail.com");
	      properties.put("mail.smtp.port", "587");
	      properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");     
	      
	      Session session = Session.getInstance(properties,new Authenticator() {
	    	  @Override
	    	protected PasswordAuthentication getPasswordAuthentication() {
	    				return new PasswordAuthentication(email,password);
	    	}
		});
	      
	      Message message=prepareMessageAdmin(session,toEmail,msgPassword,adminId);
	      Transport.send(message);
	      
	}
	
private static Message prepareMessageAdmin(Session session,String toEmail,String msgPassword,long adminId) {
		
		 try {
			 
		     MimeMessage message = new MimeMessage(session);
		     message.setFrom(new InternetAddress(email));
		     message.addRecipient(Message.RecipientType.TO,new InternetAddress(toEmail));
		     message.setSubject("BookNow@24.7 Welcomes you to The Admin team");
		     String htmlText="<h1 style=\"text-align:center; color:#023F1D\"><i>Admin Account Successfully Created</i></h1>"
		     		+ "<h3 >We are really happy to have you as a part of BookNow@24.7. </h3>"
		     		+ "<h3 style=\"margin-top: 0;\">We wish you Good Luck working with us. Hope this journey with us gives you good experience.</h3>"
		     		+"<h3 style=\"margin-top: 0;\">Here are the credentials to start working with us :</h3>"		    		
		     		+ "<div style=\" color:#023F1D;\"><h2 style=\"margin-left: 10%;\">AdminId: "+adminId+"</h2><h2 style=\"margin-left: 10%;margin-top: 10px;\">Password: "+msgPassword+
		     		" </h2></div><h3>Dont forget to reset your password.</h3></div>"+
		     		"<br><h3>-Regards</h3><h3><i>Team BookNow@24.7</i></h3><br><h5 style=\"text-align:center;\"><<<i>This is a system generated mail</i>>></h5>";
		     
		     message.setText(htmlText,"US-ASCII", "html");

		     return message;
		 
		     } catch (MessagingException e) {
		    	 e.printStackTrace();
		     }
		 
		return null;
}
//--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//				SENDS MAIL ON USER CREATION WITH AUTOMATED PASSWORD IF NOT ENTERD BY USER
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

public static void sendMailUser(String toEmail,long phoneNo,String msgPassword) throws Exception {
	  
    Properties properties = System.getProperties();

    properties.put("mail.smtp.auth", "true");
    properties.put("mail.smtp.starttls.enable", "true");
    properties.put("mail.smtp.host", "smtp.gmail.com");
    properties.put("mail.smtp.port", "587");
    properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");
	
    
    Session session = Session.getInstance(properties,new Authenticator() {
  	  @Override
  	protected PasswordAuthentication getPasswordAuthentication() {
  				return new PasswordAuthentication(email,password);
  	}
	});
    
    Message message=prepareMessageUser(session,email,toEmail,msgPassword,phoneNo);
    Transport.send(message);
  
}

private static Message prepareMessageUser(Session session, String email,String toEmail,String msgPassword,long phoneNo) {
	
	 try {
		 
	     MimeMessage message = new MimeMessage(session);
	     message.setFrom(new InternetAddress(email));
	     message.addRecipient(Message.RecipientType.TO,new InternetAddress(toEmail));
	     message.setSubject("BookNow@24.7 Welcome's You To The Family ");
	     
	     if(msgPassword!=null) {
	 		
	 		String htmlText="<h1 style=\"text-align:center; color:#023F1D\"><i>Your Account was Created Successfully </i></h1>"
		     		+ "<h3 >We are really happy to have you as a part of BookNow@24.7 family. </h3>"
		     		+ "<h3 style=\"margin-top: 0;\">Let us help you reach out to various events going on.</h3>"+
		     		"<h3 style=\"margin-top: 0;\">Hope you love our service and always seek for our services for making your bookings. </h3>"+
		     		"<h3>You can always login to view latest events and book your seats, Also you can track your previous bookings. </h3>"
		     		+"<h3 style=\"margin-top: 0;\">Here are the credentials to your account:</h3>"		    		
		     		+ "<div style=\" color:#023F1D;\"><h2 style=\"margin-left: 10%;\">Phone Number: "+phoneNo+"</h2><h2 style=\"margin-left: 10%;margin-top: 10px;\">Password: "+msgPassword+
		     		"</h2> </div><h3>Dont forget to reset your password</h3>"+
		     		"<br><h3>-Regards</h3><h3><i>Team BookNow@24.7</i></h3><br><h5 style=\"text-align:center;\"><<<i>This is a system generated mail</i>>></h5>";
		     	
	 		message.setText(htmlText,"US-ASCII", "html");
	 	}
	     else {	     
	     String htmlText="<h1 style=\"text-align:center; color:#023F1D\"><i>Your Account was Created Successfully </i></h1>"
		     		+ "<h3 >We are really happy to have you as a part of BookNow@24.7 family. </h3>"
		     		+ "<h3 style=\"margin-top: 0;\">Let us help you reach out to various events going on.</h3>"+
		     		"<h3 style=\"margin-top: 0;\">Hope you love our service and always seek for our services for making your bookings. </h3>"+
		     		"<h3>You can always login to view latest events and book your seats, Also you can track your previous bookings. </h3>"+
		     		"<br><h3>-Regards</h3><h3><i>Team BookNow@24.7</i></h3><br><h5 style=\"text-align:center;\"><<<i>This is a system generated mail</i>>></h5>";
		     	
	     	message.setText(htmlText,"US-ASCII", "html");
	     }
	     
	     return message;
	 
	     } catch (MessagingException e) {
	    	 e.printStackTrace();
	     }
	 
	return null;
}





}
