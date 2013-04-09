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
import java.beans.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;

/**
 * This class is a custom editor for the messageText property of the
 * YesNoPanel bean.  It is necessary because the default editor for
 * properties of type String does not allow multi-line strings
 * to be entered.
 */
public class YesNoPanelMessageEditor implements PropertyEditor {
    protected String value;  // The value we will be editing.
    
    public void setValue(Object o) {  value = (String) o; }
    public Object getValue() { return value; }
    public void setAsText(String s) { value = s; }
    public String getAsText() { return value; }
    public String[] getTags() { return null; }  // not enumerated; no tags
    
    // Say that we allow custom editing.
    public boolean supportsCustomEditor() { return true; }
    
    // Return the custom editor.  This just creates and returns a TextArea
    // wrapped in a JScrollPane to edit the multi-line text.
    // But it also registers a listener on the text area to update the value
    // as the user types and to fire the property change events that property
    // editors are required to fire.
    public Component getCustomEditor() {
	final JTextArea t = new JTextArea(value, 5, 30); // 5 rows, 30 cols
	t.getDocument().addDocumentListener(new DocumentListener() {
		public void insertUpdate(DocumentEvent e) { update(); }
		public void changedUpdate(DocumentEvent e) { update(); }
		public void removeUpdate(DocumentEvent e) { update(); }
		public void update() {
		    value = t.getText();
		    listeners.firePropertyChange(null, null, null);
		}
	    });

	return new JScrollPane(t);
    }
    
    // Visual display of the value, for use with the custom editor.
    // Just print some instructions and hope they fit in the in the box.
    // This could be more sophisticated.
    public boolean isPaintable() { return true; }
    public void paintValue(Graphics g, Rectangle r) {
	g.setClip(r);
	g.drawString("Click to edit...", r.x+5, r.y+15);
    }
    
    // Important method for code generators.  Note that it really ought to
    // escape any quotes or backslashes in value before returning the string.
    public String getJavaInitializationString() { return "\"" + value + "\""; }
    
    // This code uses the PropertyChangeSupport class to maintain a list of
    // listeners interested in the edits we make to the value.
    protected PropertyChangeSupport listeners =new PropertyChangeSupport(this);
    public void addPropertyChangeListener(PropertyChangeListener l) {
	listeners.addPropertyChangeListener(l);
    }
    public void removePropertyChangeListener(PropertyChangeListener l) {
	listeners.removePropertyChangeListener(l);
    }
}
