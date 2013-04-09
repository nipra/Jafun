import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.io.*;
import java.util.*;

/** TestEdit View JMenu for application */
public class TVM extends TV {
	JMenuBar mb;
	JMenu fm, em, vm, om, hm;	// File, Edit, View, Options, Help
	JCheckBoxMenuItem cb;	// Option that can be on or off.

	/** Construct the object including its GUI */
	public TVM(JFrame f, TD mod) {
		super(f, mod);

		// Set up the JMenu hierarchy
		JMenuItem mi;
		mb = new JMenuBar();
		frm.setJMenuBar(mb);		// Frame implements JMenuContainer

		// The File JMenu...
		fm = new JMenu("File");
			fm.add(mi = new JMenuItem("Open...", 'O'));
			mi.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
					theTD.loadFile(null);
				}
			});
			fm.add(mi = new JMenuItem("New..."));
			mi.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
					theTD.doNew();
				}
			});
			fm.add(mi = new JMenuItem("Save", 'S'));
			mi.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
					theTD.saveFile();
				}
			});
			fm.add(mi = new JMenuItem("Save As..."));
			mi.setEnabled(false);
			fm.add(mi = new JMenuItem("Close", 'W'));
			mi.setEnabled(false);
			fm.addSeparator();
			fm.add(mi = new JMenuItem("Export to RTF..."));
			mi.setEnabled(false);
			fm.add(mi = new JMenuItem("Export to Sylvan Prometric..."));
			mi.setEnabled(false);
			fm.add(mi = new JMenuItem("Export to HTML..."));
			mi.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
					theTD.saveHTML("test.html");
				}
			});
			fm.addSeparator();
			fm.add(mi = new JMenuItem("Print draft", 'P'));
			mi.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
					theTD.doPrint();
				}
			});
			fm.add(mi = new JMenuItem("Print Student copy"));
			mi.setEnabled(false);
			fm.addSeparator();
			fm.add(mi = new JMenuItem("Exit"));
			mi.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					theTD.exit(0);
				}
			});
		mb.add(fm);

		// The Edit JMenu...
		em = new JMenu("Edit");
			em.add(mi = new JMenuItem("Find...", 'F'));
			em.addSeparator();
			em.add(mi = new JMenuItem("Copy Entire Question"));
			mi.setEnabled(false);
			em.add(mi = new JMenuItem("Cut Entire Question"));
			mi.setEnabled(false);
			em.add(mi = new JMenuItem("Paste Question"));
			mi.setEnabled(false);
			em.addSeparator();
			em.add(mi = new JMenuItem("Preferences"));
			mi.setEnabled(false);
		mb.add(em);

		// The View JMenu...
		vm = new JMenu("View");
			vm.add(mi = new JMenuItem("Exam Information..."));
			mi.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// TD.mainPane.show("Exam Info");
				}
			});
			vm.add(mi = new JMenuItem("Statistics..."));
			mi.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
					theTD.doStats();	// calls us back at showStats();
				}
			});
			vm.addSeparator();
			vm.add(mi = new JMenuItem("Windows Look and Feel"));
			mi.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent wlfe) {
					try {
						UIManager.setLookAndFeel(
						"com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
						SwingUtilities.updateComponentTreeUI(frm);
					} catch (Exception e) {
						JOptionPane.showMessageDialog(null,
							"setLookAndFeel didn't work: " + e,
							"UI Failure",
							JOptionPane.INFORMATION_MESSAGE);
					}
				}
			});
			vm.add(mi = new JMenuItem("Java Look and Feel"));
			mi.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent jlfe) {
					try {
						UIManager.setLookAndFeel(
						"com.sun.java.swing.plaf.metal.MetalLookAndFeel");
						SwingUtilities.updateComponentTreeUI(frm);
					} catch (Exception e) {
						JOptionPane.showMessageDialog(null,
							"setLookAndFeel didn't work: " + e,
							"UI Failure",
							JOptionPane.INFORMATION_MESSAGE);
					}
				}
			});
		mb.add(vm);

		// The Options JMenu...
		om = new JMenu("Options");
			cb = new JCheckBoxMenuItem("AutoSave");
			cb.setState(true);
			om.add(cb);
		mb.add(om);

		// The Help JMenu...
		hm = new JMenu("Help");
			hm.add(mi = new JMenuItem("About"));
			mi.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(frm, 
		"TestEdit - an Exam Question Editor\n" +
		"Copyright (c) 1995-1997 by Ian F. Darwin, http://www.darwinsys.com/.\n" +
		"Information available from http://www.darwinsys.com/testedit"
					);
				}
			});
			hm.add(mi = new JMenuItem("Topics"));
			mi.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
					Window jh = new MyHelp();
					centre(jh);
					jh.setVisible(true);
				}
			});
		// mb.setHelpJMenu(hm);
		mb.add(hm);
	}
}
