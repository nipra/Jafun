import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

/** A Java Servlet to administer the tests over the Web.
 *	Saves exam and status session object to avoid having to reload it, but also
 *		to keep the exam constant during a session!
 * @version $Id: DoTestServlet.java,v 1.11 2002/04/05 17:03:36 ian Exp $
 */
public class DoTestServlet extends HttpServlet {

	/** Where to find the exams du jour */
	protected static String DIRECTORY;
	/** The body color */
	protected final static String BGCOLOR = "white";

	/** Inner class to track the student's progress */
	class Progress {
		Exam exam;			// exam being taken
		boolean done;		// exam is finished.
		String category;	// name of exam, in effect
		int  curQuest;		// Question number working on, 0-origin
		int  correct;		// number gotten right on first try
	}

	public void init() {
		DIRECTORY = getServletContext().getInitParameter("quizzes.dir");
		if (DIRECTORY == null) {
			throw new IllegalArgumentException(
				"getInitParam(quizzes.dir) returns null");
		}
		log("Quizzes: DIRECTORY set to " + DIRECTORY);
	}

	/** Service is used to service each request. */
	public void service(HttpServletRequest request,
		HttpServletResponse response) throws IOException, ServletException {

		PrintWriter out = response.getWriter();
		HttpSession session;
		Progress progress;
		String reqCategory, reqSubject;

		// Set response type to HTML. Print the HTML header.
		response.setContentType("text/html");
		out.println("<HTML>");

		// Find the requested category
		reqCategory = request.getParameter("category");
		reqSubject  = request.getParameter("subject");	// unix or java

		// Request the user's session, creating it if new.
		session = request.getSession(true);
		if (session.isNew()) {
			log("<B>NEW SESSION</B>");
			progress = new Progress();
			progress.category = reqCategory;
			session.putValue("progress", progress);
		} else {
			progress = (Progress) session.getValue("progress");
		}

		if (reqCategory != null && progress.category != null && 
			!reqCategory.equals(progress.category)) {
			
			// CHANGE OF CATEGORIES
			log("<B>NEW PROGRESS CUZ " + 
				reqCategory + " != " +progress.category + "</B>");
			progress = new Progress();
			progress.category = reqCategory;
			session.putValue("progress", progress);
		}
		if (progress.exam == null) {
			XamDataAccessor ls = new XamDataAccessor();
			try {
				progress.exam = ls.load(DIRECTORY + reqSubject + "/" +
					progress.category + ".xam");
			} catch (IOException ex) {
				eHandler(out, ex, "We had some problems loading that exam!");
			} catch (NullPointerException ex) {
				eHandler(out, ex, "Hmmm, that exam file seems to be corrupt!");
			}
		}

		// Now that we have "exam", use it to get Title. 
		out.print("<TITLE>Questions on ");
			out.print(progress.exam.getCourseTitle()); out.println("</TITLE>");
		out.print("<BODY BGCOLOR=\""); out.print(BGCOLOR); out.println("\">");
		out.print("<H1>");
			out.print(progress.exam.getCourseTitle());
			out.println("</H1>");

		// Guard against reloading last page
		if (progress.done) {
			out.println("<HR><a href=\"/quizzes/\">Another Quiz?</a>");
			out.flush();
			return;
		}

		// Are we asking a question, or marking it?
		out.println("<P>");
		String answer =request.getParameter("answer");
		int theirAnswer = -1;
		if (answer != null) {
			// MARK IT.
			Q q = progress.exam.getQuestion(progress.curQuest);
			theirAnswer = Integer.parseInt(answer);
			if (theirAnswer == q.getAns()) {

				// WE HAVE A RIGHT ANSWER -- HURRAH!
				if (!q.tried) {
					out.println("<P><B>Right first try!</B>");
					progress.correct++;
				} else
					out.println("<P><B>Right. Knew you'd get it.</B>");
				q.tried = true;			// "Tried and true..."

				if (++progress.curQuest >= progress.exam.getNumQuestions()) {
					out.print("<P>END OF EXAM.");
					if (progress.correct == progress.curQuest) {
						out.println("<P><B>Awesome!</B> You got 100% right.");
					} else {
						out.print("You got ");
						out.print(progress.correct);
						out.print(" correct out of ");
						out.print(progress.curQuest);
						out.println(".");
					}
					out.println("<HR><a href=\"/quizzes/\">Another Quiz?</a>");

					// todo invalidate "progress" in case user retries 
					progress.done = true;

					// Return, so we don't try to print the next question!
					return;

				} else {
					out.print("Going on to next question");
					theirAnswer = -1;
				}
			} else {
				out.print("<B>Wrong answer</B>. Please try again.");
				q.tried = true;
			}
		}

		// Progress?
		out.print("<P>Question ");
		out.print(progress.curQuest+1);
		out.print(" of ");
		out.print(progress.exam.getNumQuestions());
		out.print(". ");
		if (progress.curQuest >= 2) {
			out.print(progress.correct);
			out.print(" correct out of ");
			out.print(progress.curQuest);
			out.print(" tried so far (");
			double pct = 100.0 * progress.correct  / progress.curQuest;
			out.print((int) pct);
			out.println("%).");
		}

		// Now generate a form for the next (or same) question
		out.print("<FORM ACTION=/quizzes/servlet/DoTestServlet METHOD=POST>");
		out.print("<INPUT TYPE=hidden NAME=category VALUE=");
			out.print(progress.category); out.println(">");
		out.println("<HR>");

		Q q = progress.exam.getQuestion(progress.curQuest);
		out.println(q.getQText());

		for (int j=0; j<q.getNumAnswers(); j++) {
				out.print("<BR><INPUT TYPE=radio NAME=answer VALUE=\"");
				out.print(j);
				out.print("\"");
				if (j==theirAnswer)
					out.print(" CHECKED");
				out.print(">");
				out.print(q.getAnsText(j));
				out.println("</INPUT>");
			}
		out.println("<HR>");

		out.println("<INPUT TYPE=SUBMIT VALUE=\"Mark it!\"");
		out.println("</FORM>");
		out.println("</HTML>");
		out.close();
	}

	void eHandler(PrintWriter out, Exception ex, String msg) {
		out.println("<H1>Error!</H1>");
		out.print("<B>");
		out.print(msg);
		out.println("</B>");
		out.println("<pre>");
		ex.printStackTrace(out);
		out.flush();
		out.close();
	}
}
