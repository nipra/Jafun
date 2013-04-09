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
import java.net.*;
import java.io.*;

/**
 * This class defines three static methods useful for coping with unrecoverable
 * exceptions and errors.  
 * 
 * getHTMLDetails() returns an HTML-formatted stack trace suitable for display.
 *
 * reportThrowable() serializes the exception and POSTs it to a web server
 * through a java.net.URLConnection.
 *
 * displayThrowable() displays the exception's message in a dialog box that
 *   includes buttons that invoke getHTMLDetails() and reportThrowable().
 *
 * This example demonstrates: StackTraceElement, chained exceptions, 
 * Swing dialogs with JOptionPane, object serialization and URLConnection.
 */
public class ErrorHandler {
    /**
     * Display details about throwable in a simple modal dialog.  Title is the
     * title of the dialog box.  If submissionURL is not null, allow the user
     * to report the exception to that URL.  Component is the "owner" of the
     * dialog, and may be null for non-graphical applications.
     **/
    public static void displayThrowable(final Throwable throwable,
					String title,
					final String submissionURL,
					Component component)
    {
	// Get throwable class name minus the package name
	String className = throwable.getClass().getName();
	className = className.substring(className.lastIndexOf('.')+1);

	// Basic error message is className plus exception message if any.
	String msg = throwable.getMessage();
	final String basicMessage = className + ((msg != null)?(": "+msg):"");

	// Here is a JLabel to display the message.  We create the component
	// explicitly so we can manipulate it with the buttons.  Note final
	final JLabel messageLabel = new JLabel(basicMessage);

	// Here are buttons for the dialog.  They are final for use in 
	// the event listeners below.  The "Send Report" button is only
	// enabled if we have a URL to send a bug report to.
	final JButton detailsButton = new JButton("Show Details");
	final JButton reportButton = new JButton("Send Report");
	reportButton.setEnabled(submissionURL != null);

	// Our dialog will display a JOptionPane.  Note that we don't 
	// have to create the "Exit" button ourselves.  JOptionPane will
	// create it for us, and will cause it to close the dialog.
	JOptionPane pane =
	    new JOptionPane(messageLabel, JOptionPane.ERROR_MESSAGE,
			    JOptionPane.YES_NO_OPTION, null,
			    new Object[] {detailsButton,reportButton,"Exit"});

	// This is the dialog box containing the pane.
	final JDialog dialog = pane.createDialog(component, title);

	// Add an event handler for the Details button
	detailsButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent event) {
		    // Show or hide error details based on state of button.
		    String label = detailsButton.getText();
		    if (label.startsWith("Show")) {
			// JLabel can display simple HTML text
			messageLabel.setText(getHTMLDetails(throwable));
			detailsButton.setText("Hide Details");
			dialog.pack();  // Make dialog resize to fit details
		    }
		    else {
			messageLabel.setText(basicMessage);
			detailsButton.setText("Show Details");
			dialog.pack();
		    }
		}
	    });


	// Event handler for the "Report" button.
	reportButton.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent event) {
		try {
		    // Report the error, get response.  See below.
		    String response=reportThrowable(throwable, submissionURL);
		    // Tell the user about the report
		    messageLabel.setText("<html>Error reported to:<pre>" +
					 submissionURL +
					 "</pre>Server responds:<p>" +
					 response + "</html>");
		    dialog.pack();  // Resize dialog to fit new message
		    // Don't allow it to be reported again
		    reportButton.setText("Error Reported");
		    reportButton.setEnabled(false);
		}
		catch (IOException e) {  // If error reporting fails
		    messageLabel.setText("Error not reported: " + e);
		    dialog.pack();
		}
	    }
	});

	// Display the dialog modally.  This method will return only when the
	// user clicks the "Exit" button of the JOptionPane.
	dialog.show();     
    }

    /**
     * Return an HTML-formatted stack trace for the specified Throwable, 
     * including any exceptions chained to the exception.  Note the use of 
     * the Java 1.4 StackTraceElement to get stack details.  The returned 
     * string begins with "<html>" and is therefore suitable for display in
     * Swing components such as JLabel.
     */
    public static String getHTMLDetails(Throwable throwable) {
	StringBuffer b = new StringBuffer("<html>");
	int lengthOfLastTrace = 1;  // initial value

	// Start with the specified throwable and loop through the chain of
	// causality for the throwable.
	while(throwable != null) {
	    // Output Exception name and message, and begin a list 
	    b.append("<b>" + throwable.getClass().getName() + "</b>: " +
		     throwable.getMessage() + "<ul>");
	    // Get the stack trace and output each frame.  
	    // Be careful not to repeat stack frames that were already reported
	    // for the exception that this one caused.
	    StackTraceElement[] stack = throwable.getStackTrace();
	    for(int i = stack.length-lengthOfLastTrace; i >= 0; i--) {
		b.append("<li> in " +stack[i].getClassName() + ".<b>" +
			 stack[i].getMethodName() + "</b>() at <tt>"+
			 stack[i].getFileName() + ":" +
			 stack[i].getLineNumber() + "</tt>");
	    }
	    b.append("</ul>");  // end list
	    // See if there is a cause for this exception
	    throwable = throwable.getCause();
	    if (throwable != null) {
		// If so, output a header
		b.append("<i>Caused by: </i>");
		// And remember how many frames to skip in the stack trace
		// of the cause exception
		lengthOfLastTrace = stack.length;  
	    }
	}
	b.append("</html>"); 
	return b.toString();
    }

    /**
     * Serialize the specified Throwable, and use an HttpURLConnection to POST
     * it to the specified URL.  Return the response of the web server.
     */
    public static String reportThrowable(Throwable throwable,
					 String submissionURL) 
	throws IOException
    {
	URL url = new URL(submissionURL);        // Parse the URL
	URLConnection c = url.openConnection();  // Open unconnected Connection
	c.setDoOutput(true);
	c.setDoInput(true);
	// Tell the server what kind of data we're sending
	c.addRequestProperty("Content-type",
			     "application/x-java-serialized-object");
	
	// This code might work for other URL protocols, but it is intended
	// for HTTP.  We use a POST request to send data with the request.
	if (c instanceof HttpURLConnection)
	    ((HttpURLConnection)c).setRequestMethod("POST");
	
	c.connect();  // Now connect to the server
	
	// Get a stream to write to the server from the URLConnection.
	// Wrap an ObjectOutputStream around it and serialize the Throwable.
	ObjectOutputStream out =
	    new ObjectOutputStream(c.getOutputStream());
	out.writeObject(throwable);
	out.close();
	
	// Now get the response from the URLConnection.  We expect it to be
	// an InputStream from which we read the server's response.
	Object response = c.getContent();
	StringBuffer message = new StringBuffer();
	if (response instanceof InputStream) {
	    BufferedReader in =
	      new BufferedReader(new InputStreamReader((InputStream)response));
	    String line;
	    while((line = in.readLine()) != null) message.append(line);
	}
	return message.toString();
    }

    // A test program to demonstrate the class
    public static class Test {
	public static void main(String[] args) {
	    String url = (args.length > 0)?args[0]:null;
	    try { foo(); }
	    catch(Throwable e) {
		ErrorHandler.displayThrowable(e, "Fatal Error", url, null);
		System.exit(1);
	    }
	}
	// These methods purposely throw an exception
	public static void foo() { bar(null); }
	public static void bar(Object o) {
	    try { blah(o); }
	    catch(NullPointerException e) {
		// Catch the null pointer exception and throw a new exception
		// that has the NPE specified as its cause.
		throw (IllegalArgumentException)
		    new IllegalArgumentException("null argument").initCause(e);
	    }
	}
	public static void blah(Object o) {
	    Class c = o.getClass();  // throws NPE if o is null
	}
    }
}
