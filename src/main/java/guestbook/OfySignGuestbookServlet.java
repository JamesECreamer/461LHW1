package guestbook;

//https://ofyguestbookjames.appspot.com
//James Creamer jec3875
 

import com.google.appengine.api.users.User;

import com.google.appengine.api.users.UserService;

import com.google.appengine.api.users.UserServiceFactory;
import com.googlecode.objectify.Key;

import static com.googlecode.objectify.ObjectifyService.ofy;


 

import java.io.IOException;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServlet;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;

 

public class OfySignGuestbookServlet extends HttpServlet {
	
	public static boolean ListAll = false;

    public void doPost(HttpServletRequest req, HttpServletResponse resp)

                throws IOException {

        UserService userService = UserServiceFactory.getUserService();

        User user = userService.getCurrentUser();

 

        // We have one entity group per Guestbook with all Greetings residing

        // in the same entity group as the Guestbook to which they belong.

        // This lets us run a transactional ancestor query to retrieve all

        // Greetings for a given Guestbook.  However, the write rate to each

        // Guestbook should be limited to ~1/second.

        String guestbookName = req.getParameter("guestbookName");

        //Key guestbookKey = KeyFactory.createKey("Guestbook", guestbookName);

        String content = req.getParameter("content");
        
        String title = req.getParameter("title");

        Date date = new Date();
        
        Greeting greeting = new Greeting(user, content, title);

        /*Entity greeting = new Entity("Greeting", guestbookKey);

        greeting.setProperty("user", user);

        greeting.setProperty("date", date);

        greeting.setProperty("content", content);*/
        
        
        

        //DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        //datastore.put(greeting);

        ofy().save().entity(greeting).now();   // synchronous
        
        //List<Key<Greeting>> keys = ofy().load().type(Greeting.class).keys().list();
        //ofy().delete().keys(keys).now();

        resp.sendRedirect("/ofyguestbook.jsp?guestbookName=" + guestbookName);

    }
    
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException{

    	String guestbookName = req.getParameter("guestbookName");
    	
    	if(req.getParameter("subscribe") != null){
			UserService userService = UserServiceFactory.getUserService();
			User user = userService.getCurrentUser();
			if(GAEJCronServlet.emails.contains(user.getEmail()) == false){
				GAEJCronServlet.emails.add(user.getEmail());
			}
		} else if(req.getParameter("unsubscribe") != null){
			UserService userService = UserServiceFactory.getUserService();
			User user = userService.getCurrentUser();
			if(GAEJCronServlet.emails.contains(user.getEmail()) == true){
				GAEJCronServlet.emails.remove(user.getEmail());
			}
		} else if(req.getParameter("ListAll") != null){
			ListAll = !ListAll;
		}
    	
    	resp.sendRedirect("/ofyguestbook.jsp?guestbookName=" + guestbookName);
    	
	}

}