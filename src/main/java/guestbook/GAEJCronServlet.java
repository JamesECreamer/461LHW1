package guestbook;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import guestbook.Sendgrid;
import com.google.appengine.api.users.User;

import com.google.appengine.api.users.UserService;

import com.google.appengine.api.users.UserServiceFactory;
import com.googlecode.objectify.ObjectifyService;
import com.google.appengine.api.users.User;
import com.sendgrid.*;


@SuppressWarnings("serial")
public class GAEJCronServlet extends HttpServlet {

	public static final Logger _logger = Logger.getLogger(GAEJCronServlet.class.getName());

	public static ArrayList<String> emails = new ArrayList<String>();
	
	static final long DAY = 24 * 60 * 60 * 1000;
	public boolean inLastDay(Date aDate) {
	    return aDate.getTime() > System.currentTimeMillis() - DAY;
	}
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)

			throws IOException {

		try {
			
			for(int i=0; i<emails.size(); i++){
				_logger.info("Cron Job has been executed");
				_logger.info(emails.toString());
				//Put your logic here
				//BEGIN
				
				ObjectifyService.register(Greeting.class);
				
				List<Greeting> greetings = ObjectifyService.ofy().load().type(Greeting.class).list();   
				
				Collections.sort(greetings); 

				String daily = "";
				for(Greeting greeting : greetings){
					if(inLastDay(greeting.getDate())){
						daily = daily + greeting.getUser() + " at " + greeting.getDate() + ":<br>";
						if(greeting.getTitle() != null){
							daily = daily + "<b><u>" + greeting.getTitle() + "</u></b>" + "<br>";
						}
						daily = daily + greeting.getContent() + "<br><br>";
					}
				}
				
				// set credentials
				Sendgrid mail = new Sendgrid("xxxjecreamerxxx","Headshot101!");
	
				// set email data
				mail.setTo(emails.get(i)).setFrom("xxxjecreamerxxx@gmail.com").setSubject("New Posts from the Blog of James and Eric").setText(daily).setHtml(daily);
	
				// send your message
				mail.send();
			}
			
			//END

		}

		catch (Exception ex) {

			_logger.info("Your stupid");
			_logger.info(ex.toString());

		}
		

	}

	@Override

	public void doPost(HttpServletRequest req, HttpServletResponse resp)

			throws ServletException, IOException {

				doGet(req, resp);

			}

}
