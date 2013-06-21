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
 * This servlet is the server-side companion to the
 * ErrorHandler.reportThrowable() utility method developed elsewhere in this 
 * this book; it responds to the HTTP POST request initiated by that method.
 **/
public class ErrorHandlerServlet extends HttpServlet {
    // This servlet only supports HTTP POST requests
    public void doPost(HttpServletRequest request,
		       HttpServletResponse response) throws IOException
    {
	ObjectInputStream in =
	    new ObjectInputStream(request.getInputStream());
	try {
	    Throwable throwable = (Throwable) in.readObject();
	    
	    // Exercise: save the throwable to a database, along with
	    // the current time, and the IP address from which it was reported.
	    
	    // Our response will be displayed within an HTML document, but
	    // it is not a complete document itself.  Declare it plain text,
	    // but it is okay to include HTML tags in it.
	    response.setContentType("text/plain");
	    PrintWriter out = response.getWriter();
	    out.println("Thanks for reporting your <tt>" +
			throwable.getClass().getName() + "</tt>.<br>" +
			"It has been filed and will be investigated.");
	}
	catch(Exception e) {
	    // Something went wrong deserializing the object; most likely
	    // someone tried to invoke the servlet manually and didn't provide
	    // correct data.  We send an HTTP error because that is the
	    // easiest thing to do.  Note, however that none of the HTTP error
	    // codes really describes this situation adequately.
	    response.sendError(HttpServletResponse.SC_GONE,
			       "Unable to deserialize throwable object");
	}
    }
}
