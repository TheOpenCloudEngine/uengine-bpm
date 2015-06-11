package org.uengine.webservices.emailserver.impl;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.uengine.util.UEngineUtil;
import org.uengine.kernel.GlobalContext;


public class EMailServerSoapBindingImpl implements org.uengine.webservices.emailserver.EMailServer{

	static String smtpIP;
	static String userID;
	static String password;
	
	public void sendMail(java.lang.String from, java.lang.String to, java.lang.String title, java.lang.String content) throws java.rmi.RemoteException {
		sendMail(from, null, to, title, content, null, null, "UTF-8");
	}

	public void sendMail(java.lang.String mailfrom, java.lang.String mailfromName, java.lang.String mailto, java.lang.String subject, java.lang.String text, List filenames, String ccmailid, String charSet) throws java.rmi.RemoteException {
		
		if(smtpIP==null){
			smtpIP = GlobalContext.getPropertyString("emailactivity.smtp.ip", "smtp.mail.yahoo.co.kr");
			userID = GlobalContext.getPropertyString("emailactivity.smtp.userid", "pongsor");
			password = GlobalContext.getPropertyString("emailactivity.smtp.password", "18925");			
		}
		
	    try{
//	    	System.out.println("============================================");
//			System.out.println("connecting SMTP server: "+smtpIP);
//	    	System.out.println("============================================");
			
			
		    Properties props = System.getProperties();
		    // XXX - could use Session.getTransport() and Transport.connect()
		    // XXX - assume we're using SMTP
		    if (smtpIP != null)
		    	props.put("mail.smtp.host", smtpIP);
			props.put("mail.smtp.starttls.enable","true");
			props.put("mail.smtp.auth", "true");

			Session session = Session.getInstance(props, new MyPasswordAuthenticator(userID, password));

			MimeMessage mimemessage = new MimeMessage(session);
			    // set FROM
				if (UEngineUtil.isNotEmpty(mailfromName)) {
					mimemessage.setFrom(new InternetAddress(mailfrom, mailfromName, "UTF-8"));
				} else {
					mimemessage.setFrom(new InternetAddress(mailfrom));
				}
			    // set DATE
			    mimemessage.setSentDate(new java.util.Date());
			    // set SUBJECT
			    mimemessage.setSubject(encode(charSet, subject));

			    // set TO address
			    try
			    {
			        mimemessage.setRecipients(javax.mail.Message.RecipientType.TO, mailto);
			    }
			    catch(Exception exception1)
			    {
			        System.out.println("\tError in setting recipients ......\t" + exception1.getMessage());
			    }

			    // set message BODY
			    MimeBodyPart mimebodypart = new MimeBodyPart();
			    //mimebodypart.setText(text, "UTF-8");
			    mimebodypart.setContent(text, "text/html; charset=" + charSet);

			    // attach message BODY
			    MimeMultipart mimemultipart = new MimeMultipart();
			    mimemultipart.addBodyPart(mimebodypart);
			  

			    // attach FILE
			    if (filenames != null) {
				    for(Iterator iter = filenames.iterator(); iter.hasNext();){
				    	String filename = (String)iter.next();
					    if(UEngineUtil.isNotEmpty(filename)){
						    mimebodypart = new MimeBodyPart();
					        FileDataSource filedatasource = new FileDataSource(filename);
					        mimebodypart.setDataHandler(new DataHandler(filedatasource));
						    
						    File file = new File(filename);
						    
						    mimebodypart.setFileName(encode(charSet, file.getName())); // set FILENAME
						    mimemultipart.addBodyPart(mimebodypart);
					    }
				    }
			    }
			    
			    mimemessage.setContent(mimemultipart);

//			    String strResult;
			    //set CC MAIL and SEND the mail
		        // set CC MAIL
		        if(UEngineUtil.isNotEmpty(ccmailid))
		        	mimemessage.setRecipients(javax.mail.Message.RecipientType.CC, ccmailid);

		        Transport.send(mimemessage);
		        System.out.println("\tSent Successfully..........");
//		        strResult = "\tSent Successfully..........";
		}catch(Exception e){
			throw new java.rmi.RemoteException("EMailServerError:", e);
		}
	}
	
	protected String encode(String charSet, String src) throws UnsupportedEncodingException{
		return MimeUtility.encodeText(src, charSet,"B");
	}
	
	public static void main(String[] args) throws Exception{
		(new EMailServerSoapBindingImpl()).sendMail("jinyoungj@gmail.com", null, "jinyoungj@gmail.com", "������", "���� <h1>����</h1>", null, null, "EUC-KR");
	}

}
	

class MyPasswordAuthenticator extends Authenticator
{
   String user;
   String pw;
   public MyPasswordAuthenticator (String username, String password)
   {
	  super();
	  this.user = username;
	  this.pw = password;
   }
   public PasswordAuthentication getPasswordAuthentication()
   {
	  return new PasswordAuthentication(user, pw);
   }
}
