import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.mail.*;
import javax.mail.internet.*; 

/* This servlet responds to the Contact form by mailing the 
 * contact form contents "as if" from a regular user.
 * $Id: ContactServlet.java,v 1.6 2002/08/25 01:30:36 ian Exp $
 */
public class ContactServlet extends HttpServlet {
	/** The mail server, usually localhost. Should be a parameter. */
	protected String SMTPSERVER = "localhost";
	/** Contact. Gotten from a parameter. */
	protected String MYADDR;
	/** The JavaMail session object XXX Is it threadsafe? */
	protected Session session;

	public void init() throws ServletException {

		Properties props = new Properties();
		props.put("mail.smtp.host", SMTPSERVER);

		// Create the Mail Session object
		session = Session.getDefaultInstance(props, null);
		session.setDebug(true);		// Verbose!

		ServletContext ctx = getServletContext();
		MYADDR = ctx.getInitParameter("contact.email");
		if (MYADDR == null) {
			String error = "Context Parameter 'contact.email' not set!!";
			log(error);
			throw new ServletException(error);
		}
	}

	public void service(HttpServletRequest request, 
		HttpServletResponse response) 
	throws IOException, ServletException {

		ServletContext ctx = getServletContext();
		response.setContentType("text/html");

		PrintWriter out = response.getWriter();
	
		String hostIP = request.getRemoteAddr();

		String message_body = 
			"[This message is from host " +  hostIP + "]\n\n" +
			request.getParameter("message") +
			"\n";

		// out.println("<pre>" + message_body + "</pre>");

		try {

			/** Create a JavaMail message object */
			Message mesg = new MimeMessage(session);

			// TO (me) Address - should come from a Parameter...
			mesg.addRecipient(Message.RecipientType.TO,
				new InternetAddress(MYADDR));

			// FROM Address : request.name <request.email>
			mesg.setFrom(new InternetAddress(
				request.getParameter("name") + " <" +
				request.getParameter("email") + ">"));

			// The Subject
			String subject = request.getParameter("subject");
			if (subject == null || subject.length() == 0)
				subject = "DarwinSys Contact Form";
			mesg.setSubject(subject);

			// Now the message body.
			mesg.setText(message_body);
			// XXX I18N: use setText(msgText.getText(), charset)
			
			// Send the message!
			Transport.send(mesg);

			// No errors found at send time, confirm OK
			RequestDispatcher rd = 
				ctx.getRequestDispatcher("/contact-ack.jsp");
			rd.forward(request, response);

		} catch (MessagingException ex) {
			// Or something went wrong, complain to user.
			out.println("<html>");
			out.println("<head><title>Mail Error</title></head>");
			out.println("<body text=\"white\" bgcolor=\"black\">");
			out.println("<h1>Mail error</h1>");
			out.println("Rats! An error occurred sending a mail notification");
			out.println("<pre>");
			while ((ex = (MessagingException)ex.getNextException()) != null) {
				ex.printStackTrace(out);
			}
			out.println("</body>");
			out.println("</html>");
		} finally {
			out.flush();
			out.close();
		}
	}
}
