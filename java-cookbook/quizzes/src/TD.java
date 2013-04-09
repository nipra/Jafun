import java.awt.FileDialog;
import java.io.*;
import java.util.*;

/** TestEdit application model */
public class TD extends Observable {
	/** The name of this program for printing purposes */
	public final static String PROGRAM = "TestEdit";
	/** The current datafile */
	String curFileName;
	/** The main view/control */
	TV viewctl;
	/** The current XamDataAccessor object */
	XamDataAccessor ls;

	Exam curX;

	/** "main program" method - just for testing. */
	public static void main(String av[]) {
		// create a TD object
		TD td = new TD();
		td.loadFile("toy.xam");
	}

	protected void setViewCtl(TV tv) {
		viewctl = tv;
	}

	/** Construct the data model */
	public TD() {
		super();

		doNew();
	}

	protected void doNew() {
		curX = new Exam();
	}

	/** Print the current exam */
	public void doPrint() {
		new PrintDraft(curX).print();
	}

	public void doStats() {
		TStat t = new TStat();
		t.nq = curX.getListData().size();
		t.n = new int[4];
		for (int i=0; i<t.nq; i++) {
			Q tq = curX.getQuestion(i);
			if (tq.question == null || tq.question.length() == 0) {
				++t.incomplete;
				continue;
			}
			t.complete++;
			if (tq.correct == 0) {
				++t.noAnswer;
				continue;
			}
			int ans = tq.correct;
			if (ans >= 0)
				t.n[ans]++;
			if (tq.objective < 0) {
				++t.noObjective;
				continue;
			}
		}
		// System.out.println("TStats t = " + t);
		viewctl.showStats(t);
	}

	public void loadFile(String fn) {
		if (ls == null)
			ls = new XamDataAccessor();
		String newFN;
		if (fn == null) {
			viewctl.fc.setVisible(true);	// blocking dialog
			if ((newFN = ((FileDialog)viewctl.fc).getFile()) == null)
				return;
			fn = newFN;
		}
		try {
			curX = ls.load(new BufferedReader(new FileReader(curFileName=fn)));
			viewctl.installQVs();
		} catch (FileNotFoundException e) {
			System.err.println("Can't find file " + curFileName);
		} catch (IOException e) {
			System.err.println("IO Error in processing " + curFileName + ": " + e);
		} catch (Exception e) {
			System.err.println("Error in data file " + curFileName + "\n");
			e.printStackTrace();
		}
    }

	/** Save the current file */
	public synchronized void saveFile() {
		saveFile(curFileName);
	}

	/** Save the current exam into a file */
	public synchronized void saveFile(String fName) {
		// System.out.println("Saving file...");
		try {
			ls.save(new PrintWriter(new FileWriter(fName), true), curX);
		} catch (IOException e) {
			System.err.println("I/O error " + e);
		}
		// System.out.println("Save done");
	}

	/** Save the current file AS HTML. This is just an interface to
	 * the XamDataAccessorHTML object, and should be done away with, once
	 * we hava a Properties listing all the different XamDataAccessor subclasses!
	 */
	public synchronized void saveHTML(String fName) {
		// System.out.println("Saving...");
		XamDataAccessorHTML hs = new XamDataAccessorHTML();
		try {
			hs.save(new PrintWriter(new FileWriter(fName), true), curX);
		} catch (IOException e) {
			System.err.println("I/O error " + e);
		}
		// System.out.println("Save done");
	}

	/** Exit method, just calls System.exit(). Synchronized to prevent
	 * calling System.exit() during a saveFile(), which would be very bad :-)
	 */
	public synchronized void exit(int n) {
		System.exit(n);
	}

	protected void mkTitle() {
		String newt = curX.crsNum + " Exam" +
			curX.examName + " " + curX.examVers + " " + curX.crsName;
		viewctl.setTitle(newt);
	}
}
