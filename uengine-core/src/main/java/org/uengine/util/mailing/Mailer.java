package org.uengine.util.mailing;

import java.io.*;
import java.util.Properties;
import java.util.Date;

import javax.mail.*;
import javax.activation.*;
import javax.mail.internet.*;

public class Mailer {

    public static void main(String[] argv) throws Exception{
/*     		String args[]={
					"-n", "contents",			// mail
					"-o", "mailer",			
					"-M", "localhost",		// SMTP server
					"-U", "administrator",		// SMTP userid
					"-P", "18925",			// SMTP password
					"-s", "title",
					
					"pongsor2@hotmail.com"
		};
			
		send(args);*/
		send(argv);
    }
    

    public static void send(String[] argv) throws Exception{
    
		String  to, subject = null, from = null, 
			cc = null, bcc = null, url = null;
		String mailhost = null;
		String mailer = "sendhtml";
		String protocol = null, host = null, user = null, password = null;
		String record = null;	// name of folder in which to record mail
		String content= null;
		
		boolean debug = false;
		BufferedReader in =
				new BufferedReader(new InputStreamReader(System.in));
		int optind;
	
		for (optind = 0; optind < argv.length; optind++) {
		    if (argv[optind].equals("-T")) {
			protocol = argv[++optind];
		    } else if (argv[optind].equals("-H")) {
			host = argv[++optind];
		    } else if (argv[optind].equals("-t")) {
			to = argv[++optind];
		    } else if (argv[optind].equals("-n")) {
			content = argv[++optind];
		    } else if (argv[optind].equals("-U")) {
			user = argv[++optind];
		    } else if (argv[optind].equals("-P")) {
			password = argv[++optind];
		    } else if (argv[optind].equals("-M")) {
			mailhost = argv[++optind];
		    } else if (argv[optind].equals("-f")) {
			record = argv[++optind];
		    } else if (argv[optind].equals("-s")) {
			subject = argv[++optind];
		    } else if (argv[optind].equals("-o")) { // originator
			from = argv[++optind];
		    } else if (argv[optind].equals("-c")) {
			cc = argv[++optind];
		    } else if (argv[optind].equals("-b")) {
			bcc = argv[++optind];
		    } else if (argv[optind].equals("-L")) {
			url = argv[++optind];
		    } else if (argv[optind].equals("-d")) {
			debug = true;
		    } else if (argv[optind].equals("--")) {
			optind++;
			break;
		    } else if (argv[optind].startsWith("-")) {
			System.out.println(
	"Usage: sendhtml [[-L store-url] | [-T prot] [-H host] [-U user] [-P passwd]]");
			System.out.println(
	"\t[-s subject] [-o from-address] [-c cc-addresses] [-b bcc-addresses]");
			System.out.println(
	"\t[-f record-mailbox] [-M transport-host] [-d] [address]");
			System.exit(1);
		    } else {
			break;
		    }
		}

	    if (optind < argv.length) {
		// XXX - concatenate all remaining arguments
		to = argv[optind];
		System.out.println("To: " + to);
	    } else {
		System.out.print("To: ");
		System.out.flush();
		to = in.readLine();
	    }
	    if (subject == null) {
		System.out.print("Subject: ");
		System.out.flush();
		subject = in.readLine();
	    } else {
		System.out.println("Subject: " + subject);
	    }

	    Properties props = System.getProperties();
	    // XXX - could use Session.getTransport() and Transport.connect()
	    // XXX - assume we're using SMTP
	    if (mailhost != null)
		props.put("mail.smtp.host", mailhost);
		props.put("mail.smtp.starttls.enable","true");
		props.put("mail.smtp.auth", "true");

	    // Get a Session object
	    //Session session = Session.getDefaultInstance(props, null);
	    
		Session session = Session.getInstance(props, new MyPasswordAuthenticator(user, password));

	    if (debug)
			session.setDebug(true);

	    // construct the message
	    Message msg = new MimeMessage(session);
	    if (from != null)
			msg.setFrom(new InternetAddress(from));
	    else
			msg.setFrom();

	    msg.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(to, false));
	    if (cc != null)
			msg.setRecipients(Message.RecipientType.CC,
					InternetAddress.parse(cc, false));
	    if (bcc != null)
			msg.setRecipients(Message.RecipientType.BCC,
					InternetAddress.parse(bcc, false));

	    msg.setSubject(MimeUtility.encodeText(subject, "EUC-KR","B"));
		
	    if(content==null)
	    	collect(in, msg);
	    else
	    	collect(content, msg);
	    

	    msg.setHeader("X-Mailer", mailer);
	    msg.setSentDate(new Date());

	    // send the thing off
	    Transport.send(msg);

	    System.out.println("\nMail was sent successfully.");

	    // Keep a copy, if requested.

	    if (record != null) {
		// Get a Store object
		Store store = null;
		if (url != null) {
		    URLName urln = new URLName(url);
		    store = session.getStore(urln);
		    store.connect();
		} else {
		    if (protocol != null)		
			store = session.getStore(protocol);
		    else
			store = session.getStore();
		    // Connect
		    if (host != null || user != null || password != null)
			store.connect(host, user, password);
		    else
			store.connect();
		}

		// Get record Folder.  Create if it does not exist.
		Folder folder = store.getFolder(record);
		if (folder == null) {
		    throw new Exception("Can't get record folder.");
		}
		if (!folder.exists())
		    folder.create(Folder.HOLDS_MESSAGES);

		Message[] msgs = new Message[1];
		msgs[0] = msg;
		folder.appendMessages(msgs);

		System.out.println("Mail was recorded successfully.");
	    }

    }

    private static void collect(BufferedReader in, Message msg)
					throws MessagingException, IOException {
		String line;
		String subject = msg.getSubject();
		StringBuffer sb = new StringBuffer();
		sb.append("<HTML>\n");
		sb.append("<HEAD>\n");
		sb.append("<TITLE>\n");
		sb.append(subject + "\n");
		sb.append("</TITLE>\n");
		sb.append("</HEAD>\n");
	
		sb.append("<BODY>\n");
		sb.append("<H1>" + subject + "</H1>" + "\n");
	
		while ((line = in.readLine()) != null) {
		    sb.append(line);
		    sb.append("\n");
		}
	
		sb.append("</BODY>\n");
		sb.append("</HTML>\n");

		msg.setDataHandler(new DataHandler(new ByteArrayDataSource(sb.toString(), "text/html")));
    } 
    
    private static void collect(String cnt, Message msg)
					throws MessagingException, IOException {
		String line;
		String subject = msg.getSubject();
		StringBuffer sb = new StringBuffer();
		sb.append("<HTML>\n");
		sb.append("<HEAD>\n");
		sb.append("<TITLE>\n");
		sb.append(subject + "\n");
		sb.append("</TITLE>\n");
		sb.append("</HEAD>\n");
	
		sb.append("<BODY>\n");
	
		sb.append(cnt);
		
	
		sb.append("</BODY>\n");
		sb.append("</HTML>\n");
	
		//msg.setDataHandler(new DataHandler(new ByteArrayDataSource(sb.toString(), "text/html")));\
		//msg.setContent(MimeUtility.encodeText(sb.toString(), "EUC-KR", "B"), "text/html; charset=EUC-KR");\
		msg.setContent(sb.toString(), "text/html; charset=EUC-KR");
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


