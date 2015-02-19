package com.example.pogsBlog;

import java.util.List;

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

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
public class addSub extends HttpServlet {
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
                throws IOException {
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
 
        // We have one entity group per Guestbook with all Greetings residing
        // in the same entity group as the Guestbook to which they belong.
        // This lets us run a transactional ancestor query to retrieve all
        // Greetings for a given Guestbook.  However, the write rate to each
        // Guestbook should be limited to ~1/second.
        
        String guestbookName = "default";
        Key guestbookKey = KeyFactory.createKey("Sub", guestbookName);
        String sub = req.getParameter("subscriber");
        if(findSub(sub)) {
        	//unsubscribed
        	resp.addHeader("status", "unsub");
        } else {
        	Entity subEntity = new Entity("Sub", guestbookKey);
            subEntity.setProperty("subscriber", sub);
            DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
            datastore.put(subEntity);
        	resp.addHeader("status", "sub");

            //Subscribed//
        }
        
 

    }
    public boolean findSub(String user) {
    	 	DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    	    Key guestbookKey = KeyFactory.createKey("Sub", "default");
    	    // Run an ancestor query to ensure we see the most up-to-date
    	    // view of the Greetings belonging to the selected Guestbook.
    	    Query query = new Query("Sub", guestbookKey);
    	    List<Entity> greetings = datastore.prepare(query).asList(FetchOptions.Builder.withLimit(9999));
    	    for(Entity subscriber: greetings){
    	    	if(user.equals(subscriber.getProperty("subscriber"))){
    	    		datastore.delete(subscriber.getKey());
    	    		return true;
    	    	}
    	    }
    	    return false;
    }
}