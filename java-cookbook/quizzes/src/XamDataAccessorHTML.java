import java.io.*;
import java.util.*;

/** TestEdit Load/Save model - HTML version*/
public class XamDataAccessorHTML extends XamDataAccessor {

	public XamDataAccessorHTML() {
		super();
	}

    /** load one file, given an open BufferedReader */
    public Exam load(BufferedReader is) throws IOException {
		throw new IllegalArgumentException(
			"XamDataAccessorHTML cannot LOAD files");
    }

	/** Member class used to un-sort (sort randomly) list of answers */
	class link {
		/** Question Number */
		int qn;
		/** A random number */
		int r;
		/** A Question */
		Q   q;
		/** This Answer (A, B, C, or D) */
		int a;
		/** Construct a link */
		link(int qn, int r, Q q, int a) {
			this.qn = qn;
			this.r = r;
			this.q = q;
			this.a = a;
		}
	}

	public void save(PrintWriter out, Exam model) {
		Random r = new Random();
		Vector v = new Vector();
		out.println("<HTML><HEAD>");
		out.println("<TITLE>" + model.crsNum + " " + model.examName + " " + model.examVers + " " + model.crsName + "</TITLE>");
		out.println("</HEAD>");
		out.println("<BODY>");
		out.println("<H1>" + model.crsNum + " " + model.examName + " " + model.examVers + " " + model.crsName + "</H1>");
		out.println("<UL>");		// START OF PASS 1 - LIST
		for (int i=0; i<model.getNumQuestions(); i++) {
			Q q = model.getQuestion(i);
			if (q.getQText() == null || q.getQText().length() == 0)
				continue;
			out.println("<LI><A NAME=Q" + (i+1) + ">" + (i+1) + ". " + q.getQText() + "</A>");
			out.println("<UL>");		// start of one question
			for (int j=0; j<q.getCount(); j++) {
				char c = (char)('A'+j);
				out.println("<LI><A HREF=#Q"+(i+1)+"_"+c +">" + c + ". " + q.getAnsText(j) + "</A>");
				v.addElement(new link(i+1, r.nextInt(), q, j));
				}
			out.println("</UL>");		// end of one question
		} 
		out.println("</UL>");			// END OF PASS ONE

		// Depends upon JDK 1.2
		// Collections.sort(v, new Comparator() {
		// 	public int compare(Object o1, Object o2) {
		// 		link l1 = (link) o1;
		// 		link l2 = (link) o2;
		// 		if (l1.r < l2.r)
		// 			return -1;
		// 		else if (l1.r > l2.r)
		// 			return 1;
		// 		else
		// 			return 0;
		// 	}
		// });

		// PASS TWO: Print the answers; use a DL (DT and DD) for formatting
		out.println("<HR>");
		out.println("<H1>Answers - no peeking!</H1>");
		out.println("<P>The following are the answers. No peeking by reading sequentially");
		out.println("<HR>");
		out.println("<DL>");			// START PASS 2 - LIST
		for (int i=0; i<v.size(); i++) {
			link l = (link)v.elementAt(i);
			out.print("<DT><A NAME=Q"+l.qn+"_"+(char)('A'+l.a)+">"+l.q.getAnsText(l.a)+ "</A>");
			out.print("<DD>");
			out.print("<BR><BR><BR><BR><BR>");
			if (l.q.getAns() == l.a) {
				out.print("CORRECT!. <A HREF=#Q" + (l.qn+1) + ">Next question</A>");
			} else {
				out.print("Sorry, that's not the answer we expected here.<A HREF=#Q" + l.qn + ">Try again</A>");
			}
			out.println("<BR><BR><BR><BR><BR>");
		}
		out.println("</DL>");

		out.println("<H1><A NAME=Q" + (v.size()+1) + ">That's all folks!</A></H1>");
		out.println("You've done all the questions.</A>");
		out.println("<HR>");
		out.println("<A HREF=\"#top\">Back to top of page</A>");

		// print blank lines so # links near end work OK
		for (int i=0; i<10; i++) {
			out.println("<BR>");
		}
		out.println("</BODY>");
		out.println("</HTML>");
	}
}
