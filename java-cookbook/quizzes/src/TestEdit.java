import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Main Program Application Driver for TestEdit
 */
public class TestEdit {
	/** The Data Model. */
	TD theTD;
	/** The Frame, for the View to reside in */
	JFrame frm;
	/** A toolbar for editing icons */
	JToolBar toolBar;
	/** The View, and the Controllers. */
	TV vc;
	/** The splash screen */
	Splash splat = null;

	/** Main program, just to start things off. */
	public static void main(String argv[]) {
		new TestEdit(argv);
	}

	/** Construct our Main Program */
	TestEdit(String args[]) {

		// Try to make us look like a MS-Windows application
		try {
			UIManager.setLookAndFeel(
				"com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
			// No error reporting is required.
		}

		// Construct the Frame
		// System.out.println("Constructing a JFrame...");
		frm = new JFrame("TestEdit");
		Container cp = frm.getContentPane();

		// Top is a Toolbar
		toolBar = new JToolBar();
		toolBar.setFloatable(false);
		toolBar.getAccessibleContext().setAccessibleName("File Toolbar");
		cp.add(BorderLayout.NORTH, toolBar);

		JButton b;
		b = TV.addTool(toolBar, "New");
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (theTD != null)
					theTD.doNew();
			}
		});
		b = TV.addTool(toolBar, "Open");
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (theTD != null)
					theTD.loadFile(null);
			}
		});
		b = TV.addTool(toolBar, "Save");
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (theTD != null)
					theTD.saveFile();
			}
		});
		b = TV.addTool(toolBar, "Print");
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (theTD != null)
					theTD.doPrint();
			}
		});


		// Splash screen.
		splat = new Splash(frm, "TestEditSpl.gif");
		TV.centre(splat);
		splat.setVisible(true);

		// To the JFrame, add a componentListener that:
		//	1) removes itself, so this all only happens once.
		//	2) kills the splash screen when the main window appears;
		//	3) nulls the Splash object, so it can be GC'd.
		frm.addComponentListener(new ComponentAdapter() {
			public void componentShown(ComponentEvent e) {
				frm.removeComponentListener(this);
				splat.done();
				splat = null;
			}
		});

		// Construct the data model
		System.out.println("Making a TD (data model)...");
		theTD = new TD();

		// Build the Exam Info dialog and add it in.
		// Stick it in a Panel so it's not the full size of the window.
		ExamInfo exInfo = new ExamInfo(theTD);
		JPanel exInfoPanel = new JPanel();
		exInfoPanel.add(exInfo);

		// Construct the View (GUI + Controller): TVM extends TV, adds Menus.
		System.out.println("Making a ViewCtl...");
		vc = new TVM(frm, theTD);

		System.out.println("Inter-connecting the two");
		theTD.setViewCtl(vc);

		// Set up the mainpanel as a JTabbedPane, adding the ExamInfo
		// panel and the main ViewCtrl in as the only two tabs in it.
		// When done, remember to add it to the main window!
		JTabbedPane mainPane = new JTabbedPane();
		mainPane.addTab("Exam Info", exInfoPanel);
		//mainPane.setSelectedComponent(exInfoPanel);
		mainPane.addTab("Questions", vc);
		mainPane.setSelectedComponent(vc);
		cp.add(BorderLayout.CENTER, mainPane);

		// Tell the Frame what to do on CLOSE actions
        frm.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				// if unsavedChanges()
				//	prompt to save;
				theTD.exit(0);
			}
		});

		// Load the default datafile XXX make variable, remember last filename
		System.out.println("Loading your file...");
		if (args.length == 0)
			theTD.loadFile(null);
		else
			theTD.loadFile(args[0]);

		// Resize the main window, and make the Frame appear.
		System.out.println("Sizing and Showing your main window...");
		frm.pack();
		//frm.setSize(600, 500);
		TV.centre(frm);
		frm.setVisible(true);
	}
}
