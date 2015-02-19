package com.example.pogsBlog;


import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.logging.Logger;
import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
public class sendEmails extends HttpServlet {
	public final static Logger log = Logger.getLogger(sendEmails.class.getName());
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
                throws IOException {
    	 log.info("2134567890");

    	Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
 	    Key guestbookKey = KeyFactory.createKey("Sub", "default");
 	    // Run an ancestor query to ensure we see the most up-to-date
 	    // view of the Greetings belonging to the selected Guestbook.
 	    Query query = new Query("Blog", guestbookKey);
 	    List<Entity> greetings = datastore.prepare(query).asList(FetchOptions.Builder.withLimit(9999));
 	    String emailsSent = "";
 	    for(Entity subscriber: greetings){
 	    	String msgBody = "...";
 	    	 try {
 	        	 log.info("2134567890");
 	             Message msg = new MimeMessage(session);
 	             msg.setFrom(new InternetAddress("admin@totallypogs.appspotmail.com"));
 	        	 log.info("HELLO000" + (String)subscriber.getProperty("subscriber"));

 	             msg.addRecipient(Message.RecipientType.TO,
                        new InternetAddress((String)subscriber.getProperty("subscriber").toString(), "MG-MEMBER"));
 	             msg.setSubject("You have X new messages awaiting!");
 	             msg.setText(msgBody);
 	             Transport.send(msg);
 	             emailsSent += "+" + (String)subscriber.getProperty("subscriber");

 	         } catch (AddressException e) {
 	        	 log.info("HELLO000" + e.getMessage());
 	         } catch (MessagingException e) {
 	            log.info("hello111 " + e.getMessage());
 	         }
 	    }
 	    resp.sendRedirect("/?=" + 	emailsSent);
        
 
    }
}