import java.io.*;
import java.util.*;

/** TestEdit DataAccessor (aka Load/Save) - text version*/
public class XamDataAccessor {

	public XamDataAccessor() {
	}

	/** load an exam file, given its name */
	public Exam load(String name) throws IOException {
		return load(new BufferedReader(new FileReader(name)));
	}

    /** load one file, given an open BufferedReader */
    public Exam load(BufferedReader is) throws IOException {
		Exam theExam = new Exam();
		String inputLine;
		StringTokenizer st;

		if ((inputLine = is.readLine()) == null ||
			!inputLine.startsWith("X"))
			throw new IllegalArgumentException("File " + "testdata" +
				" does not begin with X line!");
		if ((st = new StringTokenizer(inputLine)).countTokens() != 4)
			throw new IllegalArgumentException("File " + "testdata" +
				" begins with invalid X line!");
		st.nextToken();
		theExam.crsNum = Integer.parseInt(st.nextToken());
		theExam.examName = st.nextToken().charAt(0);
		theExam.examVers = st.nextToken();
		System.out.println("Loading Course " + theExam.crsNum + ", " +
			"Exam " + theExam.examName + ", Version " + theExam.examVers);
		if ((inputLine = is.readLine()) == null ||
			!inputLine.startsWith("T "))
			throw new IllegalArgumentException("File " + "testdata" +
				" does not have T line second!");
		theExam.setCourseTitle(inputLine.substring(2));

		Vector al = new Vector();
		theExam.setListData(al);

			Q curQ = null;
            while ((inputLine = is.readLine()) != null) {
                // System.out.println(inputLine);
				if (inputLine.length() == 0)
					continue;
				switch(inputLine.charAt(0)) {
				case '#':
					continue;
				case 'Q':
					// System.out.println("It's a question: " + inputLine);
					curQ = new Q(4);
					al.addElement(curQ);
					curQ.setQText(inputLine.substring(4).trim(), true); // STRTOK ME
					break;
				case 'R':
					// System.out.println("It's an answer!");
					int n = inputLine.charAt(2) - 'A'; // A->0, B->1, etc.
					if (n < 0)
						break;
					curQ.setAns(n, true);
					break;
				case 'O':	// chapter objectives
					// System.out.println("It's the chapter objective!");
					int on = Integer.parseInt(inputLine.substring(2).trim());
					if (on >= 0)
						curQ.setObjective(on);
					break;
				case 'A':
				case 'B':
				case 'C':
				case 'D':
					int an = inputLine.charAt(0) - 'A'; // A->0, B->1, etc.
					curQ.setAnsText(an, inputLine.substring(2), true);
					break;
				default:
					// left over, presume multi-line question
					if (curQ == null || curQ.getQText() == null) {
						System.err.println("XamDataAccessor: ignoring " +
							inputLine);
						continue;
					}
					curQ.setQText(curQ.getQText() + "\n" + inputLine, true);
					break;
				}
        }
		is.close();
		return theExam;
    }

	public void save(PrintWriter out, Exam model) {
		out.println("X " + model.crsNum + " " + model.examName + " " + model.examVers);
		out.println("T " + model.crsName);
		out.println("N " + model.getNumQuestions());
		out.println("");
		for (int i=0; i<model.getNumQuestions(); i++) {
			Q q = (Q)model.getQuestion(i);
			if (q.getQText() == null || q.getQText().length() == 0)
				continue;
			out.println("Q " + (i+1) + " " + q.getQText());
			out.println("R " + (char)('A'+q.getAns()));
			out.println("O " + q.getObjective());
			for (int j=0; j<q.getCount(); j++)
				out.println((char)('A'+j) + " " + q.getAnsText(j));
			out.println("");
		} 
	}
}
