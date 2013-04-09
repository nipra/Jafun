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
import java.awt.event.*;
import javax.swing.*;
import java.util.List;
import java.util.ArrayList;

/**
 * This JavaBean displays a multi-line message and up to three buttons.  It
 * fires an AnswerEvent when the user clicks on one of the buttons
 **/
public class YesNoPanel extends JPanel {
    // Properties of the bean.
    protected String messageText;  // The message to display
    protected Alignment alignment; // The alignment of the message
    protected String yesLabel;     // Text for the yes, no, & cancel buttons
    protected String noLabel;
    protected String cancelLabel;
    
    // Internal components of the panel
    protected MultiLineLabel message;
    protected JPanel buttonbox;
    protected JButton yes, no, cancel;
    
    /** The no-argument bean constructor, with default property values */
    public YesNoPanel() { this("Your\nMessage\nHere"); }

    public YesNoPanel(String messageText) { 
	this(messageText, Alignment.LEFT, "Yes", "No", "Cancel");
    }
    
    /** A constructor for programmers using this class "by hand" */
    public YesNoPanel(String messageText, Alignment alignment,
		      String yesLabel, String noLabel, String cancelLabel) 
    {
	// Create the components for this panel
	setLayout(new BorderLayout(15, 15));
	
	// Put the message label in the middle of the window.
	message = new MultiLineLabel(messageText, 20, 20, alignment);
	message.setOpaque(false);  // allow background color to show through
	add(message, BorderLayout.CENTER);
	
	// Create a panel for the Panel buttons and put it at the bottom
	// of the Panel.  Specify a FlowLayout layout manager for it.
	buttonbox = new JPanel();
	buttonbox.setLayout(new FlowLayout(FlowLayout.CENTER, 25, 15));
	buttonbox.setOpaque(false);  // allow background color to show through
	add(buttonbox, BorderLayout.SOUTH);
	
	// Create each specified button, specifying the action listener
	// and action command for each, and adding them to the buttonbox
	yes = new JButton();                   // Create buttons
	no = new JButton();
	cancel = new JButton();
	// Add the buttons to the button box
	buttonbox.add(yes);
	buttonbox.add(no);
	buttonbox.add(cancel);

	// Register listeners for each button
	yes.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    fireEvent(new AnswerEvent(YesNoPanel.this,
					      AnswerEvent.YES));
		}
	    });

	no.addActionListener(new ActionListener() { 
		public void actionPerformed(ActionEvent e) {
		    fireEvent(new AnswerEvent(YesNoPanel.this,
					      AnswerEvent.NO));
			      }
	    });
	cancel.addActionListener(new ActionListener() { 
		public void actionPerformed(ActionEvent e) {
		    fireEvent(new AnswerEvent(YesNoPanel.this,
					      AnswerEvent.CANCEL));
		}
	    });
	
	// Now call property setter methods to set the message and button
	// components to contain the right text
	setMessageText(messageText);
	setAlignment(alignment);
	setYesLabel(yesLabel);
	setNoLabel(noLabel);
	setCancelLabel(cancelLabel);
    }
    
    // Methods to query all of the bean properties.
    public String getMessageText() { return messageText; }
    public Alignment getAlignment() { return alignment; }
    public String getYesLabel() { return yesLabel; }
    public String getNoLabel() { return noLabel; }
    public String getCancelLabel() { return cancelLabel; }
    public Font getMessageFont() { return message.getFont(); }
    public Color getMessageColor() { return message.getForeground(); }
    public Font getButtonFont() { return yes.getFont(); }
    
    // Methods to set all of the bean properties.
    public void setMessageText(String messageText) {
	this.messageText = messageText;
	message.setLabel(messageText);
    }

    public void setAlignment(Alignment alignment) {
	this.alignment = alignment;
	message.setAlignment(alignment);
    }

    public void setYesLabel(String l) {
	yesLabel = l;
	yes.setText(l);
	yes.setVisible((l != null) && (l.length() > 0));
    }

    public void setNoLabel(String l) {
	noLabel = l;
	no.setText(l);
	no.setVisible((l != null) && (l.length() > 0));
    }

    public void setCancelLabel(String l) {
	cancelLabel = l;
	cancel.setText(l);
	cancel.setVisible((l != null) && (l.length() > 0));
    }

    public void setMessageFont(Font f) {
	message.setFont(f);
    }
    
    public void setMessageColor(Color c) {
	message.setForeground(c);
    }
    
    public void setButtonFont(Font f) {
	yes.setFont(f);
	no.setFont(f);
	cancel.setFont(f);
    }

    /** This field holds a list of registered ActionListeners. */
    protected List listeners = new ArrayList();
    
    /** Register an action listener to be notified when a button is pressed */
    public void addAnswerListener(AnswerListener l) { listeners.add(l); }
    
    /** Remove an Answer listener from our list of interested listeners */
    public void removeAnswerListener(AnswerListener l) { listeners.remove(l); }
    
    /** Send an event to all registered listeners */
    public void fireEvent(AnswerEvent e) {
	// Make a copy of the list and fire the events using that copy.
	// This means that listeners can be added or removed from the original
	// list in response to this event.
	Object[] copy = listeners.toArray();
	for(int i = 0; i < copy.length; i++) {
	    AnswerListener listener = (AnswerListener) copy[i];
	    switch(e.getID()) {
	    case AnswerEvent.YES: listener.yes(e); break;
	    case AnswerEvent.NO:  listener.no(e); break;
	    case AnswerEvent.CANCEL: listener.cancel(e); break;
	    }
	}
    }
    
    /** A main method that demonstrates the class */
    public static void main(String[] args) {
	// Create an instance of YesNoPanel, with title and message specified:
	YesNoPanel p = new YesNoPanel("Do you really want to quit?");

	// Register an action listener for the Panel.  This one just prints
	// the results out to the console.
	p.addAnswerListener(new AnswerListener() {
		public void yes(AnswerEvent e) { System.exit(0); }
		public void no(AnswerEvent e) { System.out.println("No"); }
		public void cancel(AnswerEvent e) {
                    System.out.println("Cancel");
                }
	    });
	
	JFrame f = new JFrame();
	f.getContentPane().add(p);
	f.pack();
	f.setVisible(true);
    }
}
