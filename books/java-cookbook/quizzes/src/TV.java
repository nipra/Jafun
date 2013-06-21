import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.io.*;
import java.util.*;

/** TV - Main GUI View of one Exam. Has a JToolbar and a JPanel managed
 * by a CardLayout to show one QView at a time.
 */
public class TV extends JPanel {
	/** The data model */
	TD theTD;
	/** The Frame above us */
	JFrame frm;
	/** A toolbar for editing icons */
	JToolBar toolBar;
	/** A cardLayout for the many questions */
	CardLayout myCardLayout;
	/** A number choice entry */
	JTextField numTF;
	/** A number chooser. */
	JSlider numSlider;
	/** A panel to hold all the qview's, managed by myCardLayout */
	JPanel questionsPanel;
	/** The obligatory file chooser */
	FileDialog fc;
	/** The data */
	ExamInfo xinfo;

	/** Construct the TV object - that is, the main GUI for the program */
	public TV(JFrame f, TD mod) {
		super();
		frm = f;
		theTD = mod;
		JButton b; 

		// Build the GUI

		toolBar = new JToolBar();
		toolBar.setFloatable(false);
		toolBar.getAccessibleContext().setAccessibleName("File Toolbar");
		toolBar.addSeparator();
		b = addTool(toolBar, "Cut");
		b = addTool(toolBar, "Copy");
		b = addTool(toolBar, "Paste");
		toolBar.addSeparator();
		toolBar.putClientProperty( "JToolBar.isRollover", Boolean.FALSE );
		
		// The Slider
		numSlider= new JSlider(JSlider.HORIZONTAL, 1, 40, 1);
		numSlider.setPaintTicks(true);
		numSlider.setPaintLabels(false);
		numSlider.setMajorTickSpacing(10);
		numSlider.setMinorTickSpacing( 2);
		numSlider.setExtent(1);
		numSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent ce) {
				// System.out.println("CHANGE: " + ce);
				setQNumber(((JSlider)(ce.getSource())).getValue());
			}
		});
		numSlider.setToolTipText("Slide to select question by number");
		toolBar.add(numSlider);

		// The Question# textfield
		toolBar.add(numTF = new JTextField("01"));
		numTF.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String num = ((JTextField)e.getSource()).getText();
				int n = Integer.parseInt(num.trim());
				setQNumber(n);
			}
		});
		numTF.setToolTipText("Type number to select question by number");

		// The First Button
		b = addTool(toolBar, "First");
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setQNumber(1);
			}
		});

		// The Previous Button
		b = addTool(toolBar, "Previous");
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (getQNumber() <= 1)
					return;
				setQNumber(getQNumber() - 1);
			}
		});

		// The Next Button
		b = addTool(toolBar, "Next");
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (getQNumber() >= getNumQuestions())
					return;
				setQNumber(getQNumber() + 1);
			}
		});
  
		// The "Last" Button
		b = addTool(toolBar, "Last");
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setQNumber(getNumQuestions());
			}
		});
  
		add(BorderLayout.NORTH, toolBar); 

		// Rest is a panel to hold the questions, one at a time.
		questionsPanel = new JPanel();
		questionsPanel.setLayout(myCardLayout = new CardLayout());

		add(BorderLayout.SOUTH, questionsPanel);

		fc = new FileDialog(frm);
		fc.setFilenameFilter(new FilenameFilter() {
			public boolean accept(File ff, String fname) {
				// System.out.println("accept("+fname+")");
				// XXX TODO list of extentions, from properties.
				return fname.endsWith(".xam");
			}
		});
		TV.centre(fc);
	}

	/** Simple convenience routine for adding a button/icon to a Toolbar */
    public static JButton addTool(JToolBar toolBar, String name) {
		JButton b; 
		b = new JButton(new ImageIcon("images/" + name + ".gif",name));
		toolBar.add(b);
		b.setToolTipText(name);
		b.setMargin(new Insets(0,0,0,0));
		b.getAccessibleContext().setAccessibleName(name);
		return b;
    }

	/** Set the maximum number of questions */
	public void setNumQuestions(int i) {
		numSlider.setMaximum(i);
	}

	/** Connect the vector of QV's into the main View */
	public void installQVs() {
		Exam thisExam = theTD.curX;
		Vector qv = thisExam.getListData();
		for (int i=0; i<qv.size(); i++) {
			// Use question number (1-origin) as string name.
			// System.out.println("Add question " +i +" to " + questionsPanel);
			questionsPanel.add(Integer.toString(i+1),
				new QView((Q)qv.elementAt(i)));
		}
		myCardLayout.first(questionsPanel);
	}

	/** Change the view to display a given question number (1-origin) */
	protected void setQNumber(int nn) {
		String userShowNumber = Integer.toString(nn);

		System.out.println("setQNumber(" + userShowNumber + ");");

		// notify the model
		//	TODO -- if necessary!

		// update the view
		numTF.setText(userShowNumber);
		numSlider.setValue(nn);

		// show the right question
		myCardLayout.show(questionsPanel, userShowNumber);
	}

	/** Return the currently selected number */
	protected int getQNumber() {
		return numSlider.getValue();
	}

	/** Return the number of questions */
	protected int getNumQuestions() {
		return theTD.curX.getNumQuestions();
	}

	protected void showStats(TStat t) {
		JOptionPane.showMessageDialog(frm, t.toString());
	}

	public void setTitle(String s) {
		frm.setTitle(s);
	}
		
	/** Centre a Window on the screen */
	public static void centre(Window w) {
		Dimension us = w.getSize(), 
			them = Toolkit.getDefaultToolkit().getScreenSize();
		int newX = (them.width - us.width) / 2;
		int newY = (them.height- us.height)/ 2;
		w.setLocation(newX, newY);
	}

	/** Pure guesswork */
	public Dimension getPreferredSize() {
		return new Dimension(500, 400);
	}
}
