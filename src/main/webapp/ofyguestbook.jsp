<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ page import="java.util.List" %>

<%@ page import="com.google.appengine.api.users.User" %>

<%@ page import="com.google.appengine.api.users.UserService" %>

<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>

<%@ page import="java.util.Collections" %>

<%@ page import="com.googlecode.objectify.*" %>

<%@ page import="guestbook.Greeting" %>

<%@ page import="guestbook.OfySignGuestbookServlet" %>

<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

 

<html>

  <head>
   <link type="text/css" rel="stylesheet" href="/stylesheets/main.css" />
 </head>

 

  <body>

 

<%

    String guestbookName = request.getParameter("guestbookName");

    if (guestbookName == null) {

        guestbookName = "default";

    }

    pageContext.setAttribute("guestbookName", guestbookName);

    UserService userService = UserServiceFactory.getUserService();

    User user = userService.getCurrentUser();

    

    //DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

    //Key guestbookKey = KeyFactory.createKey("Guestbook", guestbookName);

    // Run an ancestor query to ensure we see the most up-to-date

    // view of the Greetings belonging to the selected Guestbook.

    
	ObjectifyService.register(Greeting.class);
	
	List<Greeting> greetings = ObjectifyService.ofy().load().type(Greeting.class).list();   
	
	Collections.sort(greetings); 

	if (greetings.isEmpty()) {

        %>

        <font size="4"><p align="center">This Blog has no messages.</p></font>

        <%

    } else {

        %>

        <font size="8" face="Century Gothic"><p align="center">Welcome to the Blog of James and Eric</p></font>
        
        <center><img src="https://upload.wikimedia.org/wikipedia/commons/thumb/a/a8/Texas_Longhorn_logo.svg/2000px-Texas_Longhorn_logo.svg.png" alt="longhorn.png" align="middle" style="width:256;height:128;"></center>

        <%
        
        if (user != null) {

            pageContext.setAttribute("user", user);

      %>

      <p align="center">Hello, ${fn:escapeXml(user.nickname)}! (You can

      <a href="<%= userService.createLogoutURL(request.getRequestURI()) %>">sign out</a>.)</p>

      <%

          } else {

      %>

      <p align="center">Hello!

      <a href="<%= userService.createLoginURL(request.getRequestURI()) %>">Sign in</a>

      to include your name with greetings you post.</p>

      <%

      %>

      	<p align="center"> You can post to the Blog only after you sign in. </p>

      <%

          }
        
        if(OfySignGuestbookServlet.ListAll){

	        for (Greeting greeting : greetings) {
	        	
	
	            pageContext.setAttribute("greeting_content",
	
	                                     greeting.getContent());
	            
	            pageContext.setAttribute("greeting_title",
	
	                   				 greeting.getTitle());
	
	                pageContext.setAttribute("greeting_user",
	
	                                         greeting.getUser());
	                
	                pageContext.setAttribute("greeting_date",
	
	                       					 greeting.getDate());
	
	
	            %>
	
				<p><font size="3"></font><u><b>${fn:escapeXml(greeting_title)}</b></u></font></p>
	
	            <blockquote><font size="2">${fn:escapeXml(greeting_content)}</font></blockquote>
	
	            <%
	            
	            %>
	
	            <font size="1"><p><b>${fn:escapeXml(greeting_user.nickname)}</b> wrote at <b>${fn:escapeXml(greeting_date)}</b>:</p></font>
				<hr>
	            <%
	
	        }
	
	    } else {
	    	
	    	for (int i=0; i<5; i++) {
	        	
	    		
	            pageContext.setAttribute("greeting_content",
	
	                                     greetings.get(i).getContent());
	            
	            pageContext.setAttribute("greeting_title",
	
	            						greetings.get(i).getTitle());
	
	                pageContext.setAttribute("greeting_user",
	
	                					greetings.get(i).getUser());
	                
	                pageContext.setAttribute("greeting_date",
	
	                					greetings.get(i).getDate());
	
	
	            %>
	
				<p><font size="3"></font><u><b>${fn:escapeXml(greeting_title)}</b></u></font></p>
	
	            <blockquote><font size="2">${fn:escapeXml(greeting_content)}</font></blockquote>
	
	            <%
	            
	            %>
	
	            <font size="1"><p><b>${fn:escapeXml(greeting_user.nickname)}</b> wrote at <b>${fn:escapeXml(greeting_date)}</b>:</p></font>
				<hr>
	            <%
	
	        }
	    	
	    }
    }
	
	if(!OfySignGuestbookServlet.ListAll){
	
	%>
	
		<form action="/ofysign" method="get">
	
			<div><input type="submit" name="ListAll" value="List All Posts" /></div>
	
		</form>
	
	<%

	} else {
		
		%>
		
		<form action="/ofysign" method="get">
	
			<div><input type="submit" name="ListAll" value="Reduce Posts" /></div>
	
		</form>
	
	<%
		
	}

    
    if( user != null ){
%>

    <form action="/ofysign" method="post">

	  <div><textarea placeholder="Title" name="title" rows="1" cols="60"></textarea></div>

      <div><textarea placeholder="Only memes" name="content" rows="15" cols="60"></textarea></div>

      <div><input type="submit" value="Post Greeting" /></div>
      
      <input type="hidden" name="guestbookName" value="${fn:escapeXml(guestbookName)}"/>

    </form>
    
<%



%>
    
    <form action="/ofysign" method="get">
    	
          <div onClick="alert('You have subscribed. Thank you!');"><input type="submit" name="subscribe" value="Subscribe" /></div>
    	
    </form>
    
    <form action="/ofysign" method="get">
    	
          <div onClick="alert('You have successfully unsubscribed.');"><input type="submit" name="unsubscribe" value="Unsubscribe" /></div>
    	
    </form>
    
<%
    
    }
    
 %>

  </body>

</html>