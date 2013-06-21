/** A Q sends a QChangeEvent to its observers when its question is changed.
 */
public class QChangeEvent extends java.awt.AWTEvent {
	public static final int CHANGE_QUESTION_TEXT = RESERVED_ID_MAX+1 + 0;
	public static final int CHANGE_ANSWER_NUMBER = RESERVED_ID_MAX+1 + 1;
	public static final int CHANGE_ANSWER_TEXT   = RESERVED_ID_MAX+1 + 2;
	private int ansNumber = -1;	// only for CHANGE_ANSWER_TEXT
	private String newText = null;
	Q q;

	/** Construct a given QChangeEvent */
	public QChangeEvent(Q qq, int type, int aNum, String txt) {
		super(qq, type);
		q = qq;
		ansNumber = aNum;
		newText = txt;
	}
	public int getAnsNumber() {
		return ansNumber;
	}
	public String getText() {
		return newText;
	}

	public String toString() {
		return "QChangeEvent[Question" + q + "; type " + fmt(getID()) + "]";
	}

	private String fmt(int type) {
		switch(type) {
		case CHANGE_QUESTION_TEXT:
			return "CHANGE_QUESTION_TEXT";
		case CHANGE_ANSWER_NUMBER:
			return "CHANGE_ANSWER_NUMBER";
		case CHANGE_ANSWER_TEXT:
			return "CHANGE_ANSWER_TEXT";
		default:
			return "UNKNOWN CHANGE EVENT TYPE!!";
		}
	}
}
