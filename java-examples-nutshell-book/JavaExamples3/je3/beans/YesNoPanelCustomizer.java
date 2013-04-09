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
package je3.beans;
import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;
import java.beans.*;

/**
 * This class is a customizer for the YesNoPanel bean.  It displays a
 * JTextArea and three JTextFields where the user can enter the main message
 * and the labels for each of the three buttons.  It does not allow the
 * alignment property to be set.
 **/
public class YesNoPanelCustomizer extends JComponent
    implements Customizer, DocumentListener
{
    protected YesNoPanel bean;     // The bean being customized
    protected JTextArea message;   // For entering the message    
    protected JTextField fields[]; // For entering button text

    // The bean box calls this method to tell us what object to customize.
    // This method will always be called before the customizer is displayed,
    // so it is safe to create the customizer GUI here.
    public void setObject(Object o) {
	bean = (YesNoPanel)o;   // save the object we're customizing
	
	// Put a label at the top of the panel.
	this.setLayout(new BorderLayout());
	this.add(new JLabel("Enter the message to appear in the panel:"),
		 BorderLayout.NORTH);
	
	// And a big text area below it for entering the message.
	message = new JTextArea(bean.getMessageText(), 5, 35);
	message.getDocument().addDocumentListener(this);
	this.add(new JScrollPane(message), "Center");
	
	// Then add a row of textfields for entering the button labels.
	JPanel buttonbox = new JPanel();                   // The row container
	buttonbox.setLayout(new GridLayout(1, 0, 25, 10)); // Equally spaced
	this.add(buttonbox, BorderLayout.SOUTH);           // Put row on bottom
	
	// Now go create three JTextFields to put in this row.  But actually
	// position a JLabel above each, so create an container for each
	// JTextField+JLabel combination.
	fields = new JTextField[3];               // Array of TextFields.
	String[] labels = new String[] {          // Labels for each.
	    "Yes Button Label", "No Button Label", "Cancel Button Label"};
	String[] values = new String[] {          // Initial values of each.
	    bean.getYesLabel(), bean.getNoLabel(), bean.getCancelLabel()};
	for(int i = 0; i < 3; i++) {
	    JPanel p = new JPanel();               // Create a container.
	    p.setLayout(new BorderLayout());       // Give it a BorderLayout.
	    p.add(new JLabel(labels[i]), "North"); // Put a label on the top.
	    fields[i] = new JTextField(values[i]); // Create the text field.
	    p.add(fields[i], "Center");            // Put it below the label.
	    buttonbox.add(p);                      // Add container to row.
	    // register listener for the JTextField
	    fields[i].getDocument().addDocumentListener(this);
	}
    }

    // Give ourselves some space around the outside of the panel.
    public Insets getInsets() { return new Insets(10, 10, 10, 10); }
    
    // This are the method defined by the DocumentListener interface. Whenever 
    // the user types a character in the JTextArea or JTextFields, they will be
    // called.  They all just call the internal method update() below.
    public void changedUpdate(DocumentEvent e) { update(e); }
    public void insertUpdate(DocumentEvent e) { update(e); }
    public void removeUpdate(DocumentEvent e) { update(e); }

    // Updates the appropriate property of the bean and fires a
    // property changed event, as all customizers are required to do.
    // Note that we are not required to fire an event for every keystroke.
    void update(DocumentEvent e) {
	Document doc = e.getDocument();     // What document was updated?
	if (doc == message.getDocument())   
	    bean.setMessageText(message.getText());
	else if (doc == fields[0].getDocument())
	    bean.setYesLabel(fields[0].getText());
	else if (doc == fields[1].getDocument())
	    bean.setNoLabel(fields[1].getText());
	else if (doc == fields[2].getDocument())
	    bean.setCancelLabel(fields[2].getText());
	listeners.firePropertyChange(null, null, null);
    }

    // This code uses the PropertyChangeSupport class to maintain a list of
    // listeners interested in the edits we make to the bean.
    protected PropertyChangeSupport listeners =new PropertyChangeSupport(this);
    public void addPropertyChangeListener(PropertyChangeListener l) {
	listeners.addPropertyChangeListener(l);
    }
    public void removePropertyChangeListener(PropertyChangeListener l) {
	listeners.removePropertyChangeListener(l);
    }
}
