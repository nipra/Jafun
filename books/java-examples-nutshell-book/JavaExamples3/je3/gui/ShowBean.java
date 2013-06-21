/*
 * Copyright (c) 2004 David Flanagan.  All rights reserved.
 * This code is from the book Java Examples in a Nutshell, 3nd Edition.
 * It is provided AS-IS, WITHOUT ANY WARRANTY either expressed or implied.
 * You may study, use, and modify it for any non-commercial purpose,
 * including teaching and use in open-source projects.
 * You may distribute it non-commercially as long as you retain this notice.
 * For a commercial use license, or to purchase the book, 
 * please visit http://www.davidflanagan.com/javaexamples3.
 */
package je3.gui;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.beans.*;
import java.lang.reflect.*;
import java.util.List;  // explicit import to disambiguate from java.awt.List
import java.util.*;
import java.io.*;
import je3.beans.Bean;

/**
 * This class is a program that uses reflection and JavaBeans introspection to
 * create a set of named components, set named properties on those components,
 * and display them.  It allows the user to view the components using any
 * installed look-and-feel.  It is intended as a simple way to experiment with
 * AWT and Swing components, and to view a number of the other examples
 * developed in this chapter.  It also demonstrates frames, menus, and the
 * JTabbedPane component.
 **/
public class ShowBean extends JFrame {
    // The main program
    public static void main(String[] args) {
	// Set the look-and-feel for the application.  
	// LookAndFeelPrefs is defined elsewhere in this package.
	LookAndFeelPrefs.setPreferredLookAndFeel(ShowBean.class);

	// Process the command line to get the components to display
	List beans = getBeansFromArgs(args);

	JFrame frame = new ShowBean(beans);  // Create frame

	// Handle window close requests by exiting the VM
	frame.addWindowListener(new WindowAdapter() { // Anonymous inner class
		public void windowClosing(WindowEvent e) { System.exit(0); }
	    });

	frame.setVisible(true);    // Make the frame visible on the screen

	// The main() method exits now but the Java VM keeps running because
	// all AWT programs automatically start an event-handling thread.
    }

    List beans;
    JMenu propertyMenu, commandMenu;
    JTabbedPane pane;
    JFileChooser fileChooser = new JFileChooser();

    // Most initialization code is in the constructor instead of main()
    public ShowBean(final List beans) {
	super("ShowBean");
	
	this.beans = beans;  // Save the list of Bean objects

	// Create a menubar
	JMenuBar menubar = new JMenuBar();    
	this.setJMenuBar(menubar);            // Tell the frame to display it

	// Create and populate a File menu
	JMenu filemenu = new JMenu("File");
	filemenu.setMnemonic('F');
	JMenuItem save = new JMenuItem("Save as...");
	save.setMnemonic('S');
	JMenuItem serialize = new JMenuItem("Serialize as...");
	JMenuItem quit = new JMenuItem("Quit");
	quit.setMnemonic('Q');
	menubar.add(filemenu);
	filemenu.add(save);
	filemenu.add(serialize);
	filemenu.add(new JSeparator());
	filemenu.add(quit);

	// Here are event handlers for the Save As and Quit menu items
	save.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    saveCurrentPane();
		}
	    });
	serialize.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    serializeCurrentPane();
		}
	    });
	quit.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    System.exit(0);
		}
	    });

	// Set up a menu that allows the user to select the look-and-feel of
	// the component from a list of installed look-and-feels.
	// Remember the selected LAF in a persistent Preferences node.
	JMenu plafmenu =
	  LookAndFeelPrefs.createLookAndFeelMenu(ShowBean.class,
	      new ActionListener() {
		  public void actionPerformed(ActionEvent event) {
		      // When the user selects a new LAF, tell each
		      // component to change its look-and-feel
		      SwingUtilities.updateComponentTreeUI(ShowBean.this);
		      // Then repack the frame to its new preferred size.
		      ShowBean.this.pack();
		  }
	      });
	plafmenu.setMnemonic('L');
	menubar.add(plafmenu);                // Add the menu to the menubar

	// Create the Properties and Commands menus, but don't populate them.
	// JMenuItems are added each time a new tab is selected.
	propertyMenu = new JMenu("Properties");
	propertyMenu.setMnemonic('P');
	menubar.add(propertyMenu);
	commandMenu = new JMenu("Commands");
	commandMenu.setMnemonic('C');
	menubar.add(commandMenu);
	
	// Some components have many properties, so make the property menu
	// three columns wide so that we can see all of the entries.
	propertyMenu.getPopupMenu().setLayout(new GridLayout(0,3));

	// Create a JTabbedPane to display each of the components
	pane = new JTabbedPane();
	pane.addChangeListener(new ChangeListener() {
		public void stateChanged(ChangeEvent e) {
		    populateMenus(pane.getSelectedIndex());
		}
	    });

	// Now add each component as a tab of the tabbed pane
	for(int i = 0; i < beans.size(); i++) {
	    Bean b = (Bean) beans.get(i);
	    Object o = b.getBean();
	    Component component;
	    if (o instanceof Component) component = (Component) o;
	    else component = new JLabel("This bean has no Component");
	    Image image = b.getIcon();
	    Icon icon = null;
	    if (image != null) icon = new ImageIcon(image);
	    pane.addTab(b.getDisplayName(),         // text for the tab
			icon,                       // icon for the tab
			component,                  // contents of the tab
			b.getShortDescription());   // tooltip for the tab
	}

	// Add the tabbed pane to this frame. Note the call to getContentPane()
	// This is required for JFrame, but not for most Swing components.
	this.getContentPane().add(pane);

	this.pack();   // Make frame as big as its kids need 
    }

    /**
     * This static method loops through the command line arguments looking for
     * the names of files that contain XML encoded or serialized components,
     * or for class names of components to instantiate or for name=value
     * property settings for those components. It relies on the Bean class
     * developed elsewhere.
     **/
    static List getBeansFromArgs(String[] args) {
	List beans = new ArrayList();      // List of beans to return
	Bean bean = null;                  // The current bean
	boolean expert = false;

	for(int i = 0; i < args.length; i++) {  // Loop through all arguments
	    if (args[i].charAt(0) == '-') { // Does it begin with a dash?
		try { 
		    if (args[i].equals("-expert")) {
			expert = true;
		    }
		    else if (args[i].equals("-xml")) { // read from xml
			if (++i >= args.length) continue;
			InputStream in = new FileInputStream(args[i]);
			bean = Bean.fromPersistentStream(in, expert);
			beans.add(bean);
			in.close();
		    }
		    else if (args[i].equals("-ser")) {  // deserialize a file
			if (++i >= args.length) continue;
			ObjectInputStream in =
			   new ObjectInputStream(new FileInputStream(args[i]));
			bean = Bean.fromSerializedStream(in, expert);
			beans.add(bean);
			in.close();
		    }
		    else {
			System.err.println("Unknown option: " + args[i]);
			continue;
		    }
		}
		catch(Exception e) {  // In case anything goes wrong
		    System.err.println(e);
		    System.exit(1);
		    continue;
		}
	    }
	    else if (args[i].indexOf('=') == -1){ // Its a component name
		// If the argument does not contain an equal sign, then it is
		// a component class name.
		try {
		    bean = Bean.forClassName(args[i], expert);
		    beans.add(bean);
		}
		catch(Exception e) {  
		    // If any step failed, print an error and exit
		    System.out.println("Can't load, instantiate, " +
				       "or introspect: " + args[i]);
		    System.out.println(e);
		    System.exit(1);
		}
	    }
	    else { // The arg is a name=value property specification 
		// Break into name and value parts
		int pos = args[i].indexOf('=');
		String name = args[i].substring(0, pos); // property name
		String value = args[i].substring(pos+1); // property value
		    
		// If we don't have a component to set this property on, skip!
		if (bean == null) {
		    System.err.println("Property " + name + 
				       " specified before any bean.");
		    continue;
		}

		// Now try to set the property
		try { bean.setPropertyValue(name, value); }
		catch(Exception e) {
		    // Failure to set a property is not a fatal error;
		    // Just display the message and continue
		    System.out.println(e);
		}
	    }
	}
	
	return beans;
    }

    /**
     * Ask the user to select a filename, and then save the contents of
     * the current tab to that file using the JavaBeans persistence mechanism.
     */
    public void saveCurrentPane() {
	int result = fileChooser.showSaveDialog(this);
	if (result == JFileChooser.APPROVE_OPTION) {
	    try {
		File file = fileChooser.getSelectedFile();
		XMLEncoder encoder=new XMLEncoder(new FileOutputStream(file));
		encoder.writeObject(pane.getSelectedComponent());
		encoder.close();
	    }
	    catch(IOException e) {
		JOptionPane.showMessageDialog(this, e, e.getClass().getName(),
					      JOptionPane.ERROR_MESSAGE);
	    }
	}
    }

    /**
     * Ask the user to choose a filename, and then save the contents of the
     * current pane to that file using traditional object serialization
     */
    public void serializeCurrentPane() {
	int result = fileChooser.showSaveDialog(this);
	if (result == JFileChooser.APPROVE_OPTION) {
	    try {
		File file = fileChooser.getSelectedFile();
		ObjectOutputStream out =
		    new ObjectOutputStream(new FileOutputStream(file));
		out.writeObject(pane.getSelectedComponent());
		out.close();
	    }
	    catch(IOException e) {
		JOptionPane.showMessageDialog(this, e, e.getClass().getName(),
					      JOptionPane.ERROR_MESSAGE);
	    }
	}
    }

    /**
     * Create JMenuItem objects in the Properties and Command menus.
     * This method is called whenever a new tab is selected.
     */
    void populateMenus(int index) {
	// First, delete the old menu contents
	propertyMenu.removeAll();
	commandMenu.removeAll();

	// The Bean object for this tab
	final Bean bean = (Bean) beans.get(index);

	List props = bean.getPropertyNames();
	for(int i = 0; i < props.size(); i++) {
	    final String name = (String)props.get(i);
	    // Create a menu item for the command
	    JMenuItem item = new JMenuItem(name);
	    // If the property has a non-trivial description, make it a tooltip
	    String tip = bean.getPropertyDescription(name);
	    if (tip != null && !tip.equals(name)) item.setToolTipText(tip);

	    item.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
			editProperty(bean, name);
		    }
		});

	    propertyMenu.add(item);
	}

	List commands = bean.getCommandNames();
	for(int i = 0; i < commands.size(); i++) {
	    final String name = (String)commands.get(i);
	    // Create a menu item for the command
	    JMenuItem item = new JMenuItem(name);
	    // If the command has a non-trivial description, make it a tooltip
	    String tip = bean.getCommandDescription(name);
	    if (tip != null && !name.endsWith(tip)) item.setToolTipText(tip);

	    // Invoke the command when the item is selected
	    item.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
			try { bean.invokeCommand(name); }
			catch(Exception ex) {
			    getToolkit().beep();
			    JOptionPane.showMessageDialog(ShowBean.this,
						ex, ex.getClass().getName(),
						JOptionPane.ERROR_MESSAGE);
			}
		    }
		});
	    // Add the item to the menu
	    commandMenu.add(item);
	}
	if (commands.size() == 0)
	    commandMenu.add("No Commands Available");
    }

    void editProperty(Bean bean, String name) {
	try {
	    if (bean.isReadOnly(name)) {
		String value = bean.getPropertyValue(name);
		JOptionPane.showMessageDialog(this, name + " = " + value,
					      "Read-only Property Value",
					      JOptionPane.PLAIN_MESSAGE);
	    }
	    else {
		Component editor = bean.getPropertyEditor(name);
		JOptionPane.showMessageDialog(this,
					      new Object[] { name, editor},
					      "Edit Property",
					      JOptionPane.PLAIN_MESSAGE);
	    }
	}
	catch(Exception e) {
	    getToolkit().beep();
	    JOptionPane.showMessageDialog(this, e, e.getClass().getName(),
					  JOptionPane.ERROR_MESSAGE);
	}
    }
}
