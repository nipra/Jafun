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
package je3.applet;
import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.Timer;  // not java.util.Timer
import java.text.NumberFormat;
import java.net.*;

/**
 * An applet that counts down from a specified time. When it reaches 00:00,
 * it optionally plays a sound and optionally moves the browser to a new page.
 * Place the mouse over the applet to pause the count; move it off to resume.
 * This class demonstrates most applet methods and features.
 **/
public class Countdown extends JApplet implements ActionListener, MouseListener
{
    long remaining;       // How many milliseconds remain in the countdown.
    long lastUpdate;      // When count was last updated
    JLabel label;         // Displays the count
    Timer timer;          // Updates the count every second
    NumberFormat format;  // Format minutes:seconds with leading zeros
    Image image;          // Image to display along with the time
    AudioClip sound;      // Sound to play when we reach 00:00

    // Called when the applet is first loaded
    public void init() {
	// Figure out how long to count for by reading the "minutes" parameter
	// defined in a <param> tag inside the <applet> tag.  Convert to ms.
	String minutes = getParameter("minutes");
	if (minutes != null) remaining = Integer.parseInt(minutes) * 60000;
	else remaining = 600000; // 10 minutes by default

	// Create a JLabel to display remaining time, and set some properties.
	label = new JLabel();
	label.setHorizontalAlignment(SwingConstants.CENTER); 
	label.setOpaque(true);  // So label draws the background color

	// Read some parameters for this JLabel object
	String font = getParameter("font");
	String foreground = getParameter("foreground");
	String background = getParameter("background");
	String imageURL = getParameter("image");

	// Set label properties based on those parameters
	if (font != null) label.setFont(Font.decode(font));
	if (foreground != null) label.setForeground(Color.decode(foreground));
	if (background != null) label.setBackground(Color.decode(background));
	if (imageURL != null) {
	    // Load the image, and save it so we can release it later
	    image = getImage(getDocumentBase(), imageURL);
	    // Now display the image in the JLabel.
	    label.setIcon(new ImageIcon(image));
	}	

	// Now add the label to the applet. Like JFrame and JDialog, JApplet
	// has a content pane that you add children to
	getContentPane().add(label, BorderLayout.CENTER);

	// Get an optional AudioClip to play when the count expires
	String soundURL = getParameter("sound");
	if (soundURL != null) sound=getAudioClip(getDocumentBase(), soundURL);

	// Obtain a NumberFormat object to convert number of minutes and
	// seconds to strings.  Set it up to produce a leading 0 if necessary
	format = NumberFormat.getNumberInstance();
	format.setMinimumIntegerDigits(2); // pad with 0 if necessary

	// Specify a MouseListener to handle mouse events in the applet.
	// Note that the applet implements this interface itself
	addMouseListener(this);

	// Create a timer to call the actionPerformed() method immediately,
	// and then every 1000 milliseconds. Note we don't start the timer yet.
	timer = new Timer(1000, this);
	timer.setInitialDelay(0);  // First timer is immediate.
    }

    // Free up any resources we hold; called when the applet is done
    public void destroy() { if (image != null) image.flush(); }

    // The browser calls this to start the applet running
    // The resume() method is defined below.
    public void start() { resume(); } // Start displaying updates

    // The browser calls this to stop the applet.  It may be restarted later.
    // The pause() method is defined below
    public void stop() { pause(); }   // Stop displaying updates

    // Return information about the applet
    public String getAppletInfo() {
	return "Countdown applet Copyright (c) 2003 by David Flanagan";
    }

    // Return information about the applet parameters
    public String[][] getParameterInfo() { return parameterInfo; }
    
    // This is the parameter information.  One array of strings for each
    // parameter.  The elements are parameter name, type, and description.
    static String[][] parameterInfo = {
	{"minutes", "number", "time, in minutes, to countdown from"},
 	{"font", "font", "optional font for the time display"},
	{"foreground", "color", "optional foreground color for the time"},
	{"background", "color", "optional background color"},
	{"image", "image URL", "optional image to display next to countdown"},
	{"sound", "sound URL", "optional sound to play when we reach 00:00"},
	{"newpage", "document URL", "URL to load when timer expires"},
    };

    // Start or resume the countdown
    void resume() {
	// Restore the time we're counting down from and restart the timer.
	lastUpdate = System.currentTimeMillis();
	timer.start();  // Start the timer
    }

    // Pause the countdown
    void pause() {
	// Subtract elapsed time from the remaining time and stop timing
	long now = System.currentTimeMillis();  
	remaining -= (now - lastUpdate);
	timer.stop();   // Stop the timer
    }

    // Update the displayed time.  This method is called from actionPerformed()
    // which is itself invoked by the timer.
    void updateDisplay() {
	long now = System.currentTimeMillis();  // current time in ms
	long elapsed = now - lastUpdate;        // ms elapsed since last update
	remaining -= elapsed;                   // adjust remaining time
	lastUpdate = now;                       // remember this update time

	// Convert remaining milliseconds to mm:ss format and display 
	if (remaining < 0) remaining = 0; 
	int minutes = (int)(remaining/60000);
	int seconds = (int)((remaining%60000)/1000);
	label.setText(format.format(minutes) + ":" + format.format(seconds));

	// If we've completed the countdown beep and display new page
	if (remaining == 0) {
	    // Stop updating now.
	    timer.stop();
	    // If we have an alarm sound clip, play it now.
	    if (sound != null) sound.play();
	    // If there is a newpage URL specified, make the browser
	    // load that page now.
	    String newpage = getParameter("newpage");
	    if (newpage != null) {
		try {
		    URL url = new URL(getDocumentBase(), newpage);
		    getAppletContext().showDocument(url);
		}
		catch(MalformedURLException ex) { showStatus(ex.toString()); }
	    }
	}
    }

    // This method implements the ActionListener interface.
    // It is invoked once a second by the Timer object
    // and updates the JLabel to display minutes and seconds remaining.
    public void actionPerformed(ActionEvent e) { updateDisplay(); }

    // The methods below implement the MouseListener interface.  We use 
    // two of them to pause the countdown when the mouse hovers over the timer.
    // Note that we also display a message in the statusline
    public void mouseEntered(MouseEvent e) {
	pause();                // pause countdown
	showStatus("Paused");   // display statusline message
    }
    public void mouseExited(MouseEvent e) {
	resume();               // resume countdown               
	showStatus("");         // clear statusline
    }
    // These MouseListener methods are unused.
    public void mouseClicked(MouseEvent e) {}
    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
}
