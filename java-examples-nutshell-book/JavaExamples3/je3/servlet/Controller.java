/*
 * Copyright (c) 2004 David Flanagan.  All rights reserved.
 * This code is from the book Java Examples in a Nutshell, 3nd Edition.
 * It is provided AS-IS, WITHOUT ANY WARRANTY either expressed or implied.
 * You may study, use, and modify it for any non-commercial purpose,
 * including teaching and use in open-source projects.
 * You may distribute it non-commercially as long as you retain this notice.
 * For a commercial use license, or to purchase the book, 
 * please visit http://www.davidflanagan.com/javaexamples3.
 */
package je3.servlet;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

/**
 * This is the Controller servlet for the ListManager web application.  It must
 * be configured to be invoked in response to URLs ending with ".action".  When
 * it is invoked this way, it uses the file name it is invoked as
 * ("login.action", "edit.action", etc.) to determine what to do.  Each
 * supported action name has a corresponding method which performs the
 * requested action and returns an appropriate View object in the form of a
 * a RequestDispatcher wrapped around a JSP page. The servlet dispatches to the
 * JSP page which generates an HTML document to display to the user.
 */
public class Controller extends HttpServlet {
    Connection db;                // Database connection
    UserFactory userFactory;      // Factory for managing User objects

    /**
     * This method is called when the servlet is first created. It reads its
     * initialization parameters and uses them to connect to a database.
     * It uses the database connection to create a UserFactory object.
     */
    public void init() throws ServletException {
	// Read initialization parameters from the web.xml deployment file
	ServletConfig config = getServletConfig();
	String jdbcDriver = config.getInitParameter("jdbcDriver");
	String jdbcURL = config.getInitParameter("jdbcURL");
	String jdbcUser = config.getInitParameter("jdbcUser");
	String jdbcPassword = config.getInitParameter("jdbcPassword");
	String tablename = config.getInitParameter("tablename");

	// Use those parameters to connect to the database
	try {
	    // Load the driver class.
	    // It registers itself; we don't need to retain the returned Class
	    Class.forName(jdbcDriver);

	    // Connect to database.  If the database server ever crashes,
	    // this Connection object will become invalid, and the servlet
	    // will crash too, even if the database server has come back up.
	    db = DriverManager.getConnection(jdbcURL, jdbcUser, jdbcPassword);

	    // Use the DB connection to instantiate a UserFactory object
	    userFactory = new UserFactory(db, tablename);
	}
	catch(Exception e) {
	    log("Can't connect to database", e);
	    throw new ServletException("Can't connect to database", e);
	}

	// Save an init param where our JSP pages can find it.  They need
	// this so they can display the name of the mailing list.
	ServletContext context = config.getServletContext();
	context.setAttribute("listname",config.getInitParameter("listname"));
    }

    /**
     * If the servlet is destroyed, we need to release the database connection
     */
    public void destroy() {
	try { if (db != null) db.close(); }
	catch(SQLException e) {}
    }

    /* Handle POST requests as if they were GET requests */
    public void doPost(HttpServletRequest req,HttpServletResponse resp)
	throws IOException, ServletException 
    {
	doGet(req, resp);
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
	throws IOException, ServletException 
    {
	// Look up the information we need to dispatch this request
	// We need to know what name we were invoked under and whether there
	// is already a User object in the session.
	String name = req.getServletPath();
	User user = (User) req.getSession(true).getAttribute("user");

	// This will hold the page we dispatch to for the response
	RequestDispatcher nextPage; 

	// If no user is defined yet, go to the login page.
	// Otherwise, dispatch to one of the methods below based on the name
	// by which we were invoked (see web.xml for the mapping).  The page
	// to display is the return value of the method we dispatch to.
	try {
	    if (name.endsWith("/login.action"))
		nextPage = login(req, resp);
	    else if (user == null)
		nextPage = req.getRequestDispatcher("login.jsp");
	    else if (name.endsWith("/edit.action"))
		nextPage = edit(req, resp);
	    else if (name.endsWith("/unsubscribe.action"))
		nextPage = unsubscribe(req, resp);
	    else if (name.endsWith("/logout.action"))
		nextPage=logout(req, resp);
	    else {
		// If we don't recoginze the name we're invoked under, 
		// send a HTTP 404 error.
		resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Not Found");
		return;
	    }
	}
	catch(SQLException e) {
	    // If anything goes wrong while processing the action, then
	    // output an error page.  This demonstrates how a servlet can 
	    // produce its own output instead of forwarding to a JSP page.
	    // We could also use resp.sendError() here to send an error code.
	    resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	    resp.setContentType("text/html");
	    PrintWriter out = resp.getWriter();
	    out.print("<h1>Error</h1>");
	    out.print("An unexpected error has occurred.<pre>");
	    out.print(e);
	    out.print("</pre>Please contact the webmaster.");
	    return;
	}
	// Now send the nextPage as the response to the client.
	// See the RequestDispatcher class.
	nextPage.forward(req, resp);
    }

    // This method handles "/login.action", which is either a request to
    // subscribe a new user, or a request to log in an existing subscriber.
    // A form that links to "/login.action" must define a parameter named
    // "email" and a parameter named "password".  If this is a subscription
    // request for a new user the parameter "subscribe" must also be defined.
    RequestDispatcher login(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException, SQLException
    {
	// This action can dispatch to one of two pages
	RequestDispatcher loginPage = req.getRequestDispatcher("login.jsp");
	RequestDispatcher editPage = req.getRequestDispatcher("edit.jsp");

	// Get parameters from the request
	String email = req.getParameter("email");
	String password = req.getParameter("password");

	// Make sure e-mail address is not the empty string!
	if (email.length() == 0) {
	    req.setAttribute("loginMessage",
			     "You must specify an e-mail address");
	    return loginPage;
	}

	// Now try to subscribe or login.  If all goes well, we'll end up
	// with a User objectl
	User user = null;
	try {
	    // This action is either for subscribing a new user or for 
	    // logging in an existing user.  It depends on which submit button
	    // was pressed.
	    if (req.getParameter("subscribe") != null) {
		// A new subscription
		user = userFactory.insert(email, password);
	    }
	    else {
		// A login
		user = userFactory.select(email, password);
	    }
	}
	// If anything goes wrong, we send the user back to the login page
	// with an error message.
	catch(UserFactory.NoSuchUser e) {
	    req.setAttribute("loginMessage", "Unknown e-mail address.");
	    return loginPage;
	}
	catch(UserFactory.BadPassword e) {
	    req.setAttribute("loginMessage", "Incorrect Password");
	    return loginPage;
	}
	catch(UserFactory.UserAlreadyExists e) {
	    req.setAttribute("loginMessage", email + " is already subscribed");
	    return loginPage;
	}

	// If we got here, the user is subscribed or logged in. Store the User
	// object in the current session and move on to the edit page.
	HttpSession session = req.getSession(true);
	session.setAttribute("user", user);
	return editPage;
    }

    // This method handles the URL "/edit.action".
    // A form that links to this URL must define parameters "html" and
    // "digest" if the user wants HTML messages or digests.
    RequestDispatcher edit(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException, SQLException
    {
	// Update the user's email delivery preferences based on request params
	User user = (User) req.getSession().getAttribute("user");
	user.setPrefersHTML(req.getParameter("html") != null);
	user.setPrefersDigests(req.getParameter("digest") != null);
	// Ask the factory to save the new preferences to the database
	userFactory.update(user);
	// And re-display the edit page
	return req.getRequestDispatcher("edit.jsp");
    }

    // This method handles the URL "/unsubscribe.action"
    // No parameters are necessary for this action.
    RequestDispatcher unsubscribe(HttpServletRequest req,
				  HttpServletResponse resp)
	throws ServletException, IOException, SQLException
    {
	// Get the User object from the session
	User user = (User) req.getSession().getAttribute("user");
	// Note the e-mail address before destroying it.
	String email = user.getEmailAddress();
	// Delete the suer from the database
	userFactory.delete(user);
	// Terminate the session
	req.getSession().invalidate(); // log out
	// Now display the login page again with a unsubscribed message
	req.setAttribute("loginMessage", 
			 email + " unsubscribed and logged out.");
	return req.getRequestDispatcher("login.jsp");
    }

    // This method handles the URL "/logout.action".
    // No parameters are necessary for this action.
    RequestDispatcher logout(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException, SQLException
    {
	// Destroy the session object, and return to the login page with 
	// a "logged out" message for confirmation.
	User user = (User) req.getSession().getAttribute("user");
	req.setAttribute("loginMessage", user.getEmailAddress()+" logged out");
	req.getSession().invalidate(); // delete session
	return req.getRequestDispatcher("login.jsp");
    }
}
