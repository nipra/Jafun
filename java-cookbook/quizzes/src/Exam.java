import java.util.*;

/** TestEdit application model - information about one exam */
public class Exam extends Observable {

	/** the array of questions */
	private Vector questions;
	/** The number of questions this exam should have */
	private int numQuestions;

	/** Construct a new Exam */
	Exam() {
		questions = new Vector();
	}

	/** Install/replace the list of questions. */
	void setListData(Vector v) {
		for (int i=0; i<v.size(); i++)
			if (!(v.get(i) instanceof Q))
				throw new IllegalArgumentException(
					"setListData: must be a Vector of Q objects.");
		questions = v;
	}

	/** Export the questions. There MUST be a better way! */
	public Vector getListData() {
		return questions;
	}

	/** Add a question to the list. Construct and add its QView */
	void addQuestion(Q q) {
		questions.addElement(q);
	}

	Q getQuestion(int i) {
		return (Q)questions.elementAt(i);
	}

	/** The current course name */
	protected String crsName;

	public void setCourseTitle(String s) {
		crsName = s;
	}
	public String getCourseTitle() {
		return crsName;
	}

	/** The current course number */
	protected int crsNum;

	public void setCourseNumber(String s) {
		crsNum = Integer.parseInt(s);
	}
	public String getCourseNumber() {
		return Integer.toString(crsNum);
	}

	/** the current exam (a, b, or c) */
	protected char examName;

	public void setExamName(char c) {
		examName = c;
	}
	public char getExamName() {
		return examName;
	}

	/** the current exam version (A.1, etc) */
	protected String examVers;

	public void setExamVers(String s) {
		examVers = s;
	}
	public String getExamVers() {
		return examVers;
	}

	public int getNumQuestions() {
		return questions.size();
	}
}
