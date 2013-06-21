import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.io.*;
import java.util.*;

/** Pane for editing the Exam info. Instead of a full MVC paradigm,
 * we re-fetch the information whenever setVisible(true) is done,
 * and store the information whenever our Apply button is pushed.
 *
 * @author Ian F. Darwin, http://www.darwinsys.com/
 */
public class ExamInfo extends JPanel {
	/** The Data Model */
	TD theTD;
	/** The Exam part of it. */
	Exam theExam;
	/** Textfield for the Course Title */
	JTextField cTitle;
	/** Textfield for the Course Number */
	JTextField cNum;
	/** Textfield for the Exam Number */
	JTextField xNum;
	/** Textfield for the Course Version# */
	JTextField xVers;
	/** Textfield for the "Objectives" label */
	JTextField oLabel;
	/** Textfield for the Number of Questions */
	JTextField numQuestions;

	/** Construct an ExamInfo Dialog with a TD model */
	public ExamInfo(TD m) {
		// super("Exam Info");
		theTD = m;
		theExam = m.curX;

		// Container cp = getContentPane();	// in a Frame
		Container cp = this;				// in a Panel
		cp.setLayout(new GridLayout(0,2));

		cp.add(new JLabel("Course Title", JLabel.RIGHT));
		cp.add(cTitle = new JTextField("Coffee programming for Newbies Hands-On"));

		cp.add(new JLabel("Course Number", JLabel.RIGHT));
		cp.add(cNum = new JTextField("471"));
		cNum.setToolTipText("Number of this course");

		cp.add(new JLabel("Exam Number", JLabel.RIGHT));
		cp.add(xNum = new JTextField("A"));
		xNum.setToolTipText("Exam (A, B, or C)");

		cp.add(new JLabel("Exam Revision", JLabel.RIGHT));
		cp.add(xVers = new JTextField("D.1"));
		xVers.setToolTipText("Exam Version (A.1, ...)");

		cp.add(new JLabel("Objectives Label", JLabel.RIGHT));
		cp.add(oLabel = new JTextField(""));
		oLabel.setToolTipText("Chapter, Page, or Section reference");

		cp.add(new JLabel("Number of questions", JLabel.RIGHT));
		cp.add(numQuestions = new JTextField("99"));
		numQuestions.setToolTipText("Number of questions in this Exam");

		JButton b;
		cp.add(b = new JButton("Apply"));
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ce) {
				if (theTD == null)
					System.out.println("Changes would be applied");
				else {
					setValues();
				}
				// setVisible(false);
				// dispose();
			}
		});
		cp.add(b = new JButton("Cancel"));
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ce) {
				System.out.println("No changes applied");
				// setVisible(false);
				// dispose();
			}
		});
		// pack();
	}

	/** Construct an ExamInfo Dialog with NO TD model */
	public ExamInfo() {
		this(null);
	}

	/** Whenever Apply is pushed, store the information back to the model. */
	protected void setValues() {
		theExam.setCourseTitle(cTitle.getText());
		theExam.setCourseNumber(cNum.getText());
		// XXX
	}

	/** Whenever we're displayed, update the information */
	public void setVisible(boolean vis) {
		if (vis && theTD!=null) {
			cTitle.setText(theExam.getCourseTitle());
			cNum.setText(theExam.getCourseNumber());
			// XXX
		}
		// super.setVisible(vis);
	}

	public static void main(String a[]) {
		Frame frm = new Frame("Testing ExamInfo");
		frm.add(new ExamInfo());
		frm.pack();
		frm.setVisible(true);
	}
}
