package com.example.pogsBlog;
import java.util.Date;
 
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
//@Entity
public class BlogPost implements Comparable<BlogPost> {
   // @Id Long id;
    User user;
    String content;
    Date date = new Date();
    Key blogKey = KeyFactory.createKey("blog", "blog");
    private BlogPost() {}
    public BlogPost(String title, String post, User user) {

        this.content = content;
        date = new Date();
    }
    public User getUser() {
        return user;
    }
    public String getContent() {
        return content;
    }
    @Override
    public int compareTo(BlogPost other) {
        if (date.after(other.date)) {
            return 1;
        } else if (date.before(other.date)) {
            return -1;
        }
        return 0;
    }
}