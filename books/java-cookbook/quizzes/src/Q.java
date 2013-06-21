/**
 * A Q is one Question used in TestEdit.
 */
public class Q extends java.util.Observable {
	public static final String labels[] = {
			"A", "B", "C", "D"
	};
	/** The text of the question */
	String   question;
	/** The text of the alternative answers. */
	String[] ans;
	/** The number of the answer that is correct. Zero-based. */
	int      correct;
	/** The number of the chapter that this question relates to */
	int	objective;
	/** A Boolean for use by Interactive Programs */
	boolean tried = false;

	public String toString() {
		return "Q[" + question + "]";
	}

	/** construct a Q given the # of answers allowed. */
	Q(int n) {
		if (n<0 || n>10)
			throw new IllegalArgumentException("Q.init: Count " + n + " invalid");
		question = "";
		ans = new String[n];
		correct = objective = -1;
	}
	/** Get the number of questions.
	 * @deprecated See getNumAnswers.
	 */
	public int getCount() {
		return getNumAnswers();
	}
	/** Get the number of questions. */
	public int getNumAnswers() {
		return ans.length;
	}
	public int getAns() {
		return correct;
	}
	public void setAns(int i, boolean notify) {
		if (i<0 || i>=getCount())
			throw new IllegalArgumentException("Q.setAns: Count " + i + " invalid");
		correct = i;
		setChanged();
		// if (notify)
		// 	notifyObservers(new QChangeEvent(this,
		// 		QChangeEvent.CHANGE_ANSWER_NUMBER, i, null));
	}

	void setQText(String s, boolean notify) {
		question = s;
		setChanged();
		// if (notify)
		// 	notifyObservers(new QChangeEvent(this,
		// 		QChangeEvent.CHANGE_QUESTION_TEXT, -1, s));
	}
	String getQText() {
		return question;
	}

	void setAnsText(int i, String s, boolean notify) {
		if (i<0 || i>=getCount())
			throw new IllegalArgumentException("Q.setAnsText: Count " + i + " invalid");
		ans[i] = s;
		setChanged();
		// if (notify)
		// 	notifyObservers(new QChangeEvent(this,
		// 		QChangeEvent.CHANGE_ANSWER_TEXT, i, s));
	}
	String getAnsText(int i) {
		return ans[i];
	}

	void setObjective(int i) {
		objective = i;
	}
	int getObjective() {
		return objective;
	}
}
