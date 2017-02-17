package guestbook;

import java.util.Date;

 

import com.google.appengine.api.users.User;

import com.googlecode.objectify.annotation.Entity;

import com.googlecode.objectify.annotation.Id;

 

 

@Entity

public class Greeting implements Comparable<Greeting> {

    @Id Long id;

    User user;

    String content;
    
    String title;

    Date date;

    private Greeting() {}

    public Greeting(User user, String content, String title) {

        this.user = user;

        this.content = content;

        this.title = title;
        
        date = new Date();

    }

    public User getUser() {

        return user;

    }

    public String getContent() {

        return content;

    }
    
    public String getTitle() {
    	
    	return title;
    	
    }
    
    public Date getDate() {
    	
    	return date;
    	
    }

    @Override

    public int compareTo(Greeting other) {

        if (date.after(other.date)) {

            return -1;

        } else if (date.before(other.date)) {

            return 1;

        }

        return 0;

    }

}