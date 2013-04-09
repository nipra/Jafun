import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;

/** The View/Controller for ONE QUESTION in the TestEdit program.
 * This is normally used in e.g., a TabPanel, to show one question.
 * It would be lower overhead, but not true MVC, to have one of these
 * and just load its contents when the question number changes.
 */
public class QView extends JPanel implements Observer {
	/** The question's text */
	protected JTextArea quesText;
	/** The question as a Q object */
	protected Q q;
	/** The choices - CheckBox */
	protected JCheckBox cb[];
	/** A ButtonGroup to make cb[] act as a radiobutton */
	protected ButtonGroup cbg;
	/** The choices - answer texts */
	protected JTextArea tf[];
	/** Chapter Number Chooser */
	protected JComponent cnc;

	/** Construct a QView, given the Q it is to maintain */
	QView(Q q) {
		this.q = q;
		setupGUI();

		loadGUI();
	}

	private void setupGUI() {
		GridBagLayout gbl = new GridBagLayout();
		setLayout(gbl);
		
		// Top Left, and wide: the question text
		GridBagConstraints gbcQuesText = new GridBagConstraints();
		gbcQuesText.fill = GridBagConstraints.BOTH;
		gbcQuesText.gridx = 0;
		gbcQuesText.gridwidth = 5;
		gbcQuesText.weightx = 1.0;
		gbcQuesText.weighty = 1.0;

		// Top Right Left, and narrow: for the Chapter Number label
		GridBagConstraints gbcLabel = new GridBagConstraints();
		gbcLabel.fill = GridBagConstraints.BOTH;
		gbcLabel.gridwidth = GridBagConstraints.RELATIVE;
		gbcLabel.gridx = 5;
		gbcLabel.weighty = 1.0;

		// Top Right Right, and narrow: for the Chapter Number chooser
		GridBagConstraints gbcChoice = new GridBagConstraints();
		gbcChoice.fill = GridBagConstraints.BOTH;
		gbcChoice.gridwidth = GridBagConstraints.REMAINDER;
		gbcChoice.gridx = 7;
		gbcChoice.weighty = 1.0;

		// Left and narrow: for the CheckBoxes
		GridBagConstraints gbcCheckBox = new GridBagConstraints();
		gbcCheckBox.fill = GridBagConstraints.BOTH;
		gbcCheckBox.gridwidth = 1;
		gbcCheckBox.gridx = 0;
		gbcCheckBox.weighty = 1.0;
		gbcCheckBox.anchor = GridBagConstraints.EAST;

		// Right and wide: for the text question
		GridBagConstraints gbcAnsText = new GridBagConstraints();
		gbcAnsText.fill = GridBagConstraints.BOTH;
		gbcAnsText.gridwidth = GridBagConstraints.REMAINDER;
		gbcAnsText.gridx = GridBagConstraints.RELATIVE;
		gbcAnsText.weightx = 1.0;
		gbcAnsText.weighty = 1.0;

		// Now add stuff, starting with the question text
		quesText = new JTextArea(3,80);
		quesText.setLineWrap(true);
		gbl.setConstraints(quesText, gbcQuesText);
		add(quesText);

		JLabel cnl = new JLabel("Chapter", JLabel.RIGHT);
		gbl.setConstraints(cnl, gbcLabel);
		add(cnl);

		cnc = new JTextField("    0");
		gbl.setConstraints(cnc, gbcChoice);
		add(cnc);

		quesText.getDocument().addDocumentListener(new QListener(q, -1, quesText));
		quesText.setBackground(Color.pink);

		cbg = new ButtonGroup();
		cb = new JCheckBox[q.getCount()];
		tf = new JTextArea[q.getCount()];

		for (int i=0; i<q.getCount(); i++) {
			cb[i] = new JCheckBox(Q.labels[i], false);
			cb[i].setBackground(Color.red);
			gbl.setConstraints(cb[i], gbcCheckBox);
			add(cb[i]);
			cbg.add(cb[i]);
			tf[i] = new JTextArea(2,80);
			tf[i].setBackground(Color.green);
			add(tf[i]);
			gbl.setConstraints(tf[i], gbcAnsText);
			add(tf[i]);
			tf[i].setLineWrap(true);
			tf[i].getDocument().addDocumentListener(new QListener(q, i, tf[i]));
		}
		q.addObserver(this);
	}

	private void loadGUI() {
		quesText.setText(q.getQText());
		int theAnswer = q.getAns();
		if (theAnswer >= 0) {
			cb[theAnswer].setSelected(true);
		}
		// int nQs = q.getNum();
		// tf[q.getAns()].setText(ch.getText());
	}

	/** update is called from the model when its data changes. */
	public void update(Observable q, Object change) {
		// System.out.println("UPDATE: " + q + "-->" + change);
		QChangeEvent ch = (QChangeEvent)change;
		switch(ch.getID()) {
		case QChangeEvent.CHANGE_QUESTION_TEXT:
			quesText.setText(ch.getText());
			break;
		case QChangeEvent.CHANGE_ANSWER_NUMBER:
			cb[ch.getAnsNumber()].setSelected(true);
			break;
		case QChangeEvent.CHANGE_ANSWER_TEXT  :
			tf[ch.getAnsNumber()].setText(ch.getText());
			break;
		default:
			throw new IllegalArgumentException("QVIEW.update: " + change);
		}
	}

	/** A QListener is the text changed listener for one textArea;
	 * it updates one String in the Q object.
	 */
	class QListener implements DocumentListener {
		Q q;
		int whichAns;
		JTextArea textA;
		QListener(Q q, int i, JTextArea tf) {
			this.q = q;
			whichAns = i;
			textA = tf;
		}
		public void changedUpdate(DocumentEvent de) {
			textValueChanged();
		}
		public void insertUpdate(DocumentEvent de) {
			textValueChanged();
		}
		public void removeUpdate(DocumentEvent de) {
			textValueChanged();
		}
		private void textValueChanged() {
			// System.out.println("text changed in " + textA);
			if (whichAns < 0)
				q.setQText(textA.getText(), false);
			else
				q.setAnsText(whichAns, textA.getText(), false);
		}
	}
}
