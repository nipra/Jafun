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
import javax.swing.*;
import java.awt.event.*;
import java.util.prefs.Preferences;

public class LookAndFeelPrefs {
    public static final String PREF_NAME = "preferredLookAndFeelClassName";

    /**
     * Get the desired look and feel from a per-user preference.  If
     * the preferences doesn't exist or is unavailable, use the
     * default look and feel.  The preference is shared by all classes
     * in the same package as prefsClass.
     **/
    public static void setPreferredLookAndFeel(Class prefsClass) {
	Preferences prefs=Preferences.userNodeForPackage(prefsClass);
	String defaultLAF = UIManager.getSystemLookAndFeelClassName();
	String laf = prefs.get(PREF_NAME, defaultLAF);
	try { UIManager.setLookAndFeel(laf); }
	catch (Exception e) { // ClassNotFound or InstantiationException
	    // An exception here is probably caused by a bogus preference.
	    // Ignore it silently; the user will make do with the default LAF.
	}
    }

    /**
     * Create a menu of radio buttons listing the available Look and Feels.
     * When the user selects one, change the component hierarchy under frame
     * to the new LAF, and store the new selection as the current preference
     * for the package containing class c.
     **/
    public static JMenu createLookAndFeelMenu(final Class prefsClass,
					      final ActionListener listener)
    {
	// Create the menu
	final JMenu plafmenu = new JMenu("Look and Feel");

	// Create an object used for radio button mutual exclusion
	ButtonGroup radiogroup = new ButtonGroup();  

	// Look up the available look and feels
	UIManager.LookAndFeelInfo[] plafs=UIManager.getInstalledLookAndFeels();

	// Find out which one is currently used
	String currentLAFName=UIManager.getLookAndFeel().getClass().getName();

	// Loop through the plafs, and add a menu item for each one
	for(int i = 0; i < plafs.length; i++) {
	    String plafName = plafs[i].getName();
	    final String plafClassName = plafs[i].getClassName();

	    // Create the menu item
	    final JMenuItem item =
		plafmenu.add(new JRadioButtonMenuItem(plafName));
	    item.setSelected(plafClassName.equals(currentLAFName));
	    
	    // Tell the menu item what to do when it is selected
	    item.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent event) {
		    // Set the new look and feel
		    try { UIManager.setLookAndFeel(plafClassName); }
		    catch(UnsupportedLookAndFeelException e) {
			// Sometimes a Look-and-Feel is installed but not
			// supported, as in the Windows LaF on Linux platforms.
			JOptionPane.showMessageDialog(plafmenu,
			      "The selected Look-and-Feel is " +
			      "not supported on this platform.",
			      "Unsupported Look And Feel",
			      JOptionPane.ERROR_MESSAGE);
			item.setEnabled(false);
		    }
		    catch (Exception e) { // ClassNotFound or Instantiation
			item.setEnabled(false);  // shouldn't happen
		    }

		    // Make the selection persistent by storing it in prefs.
		    Preferences p = Preferences.userNodeForPackage(prefsClass);
		    p.put(PREF_NAME, plafClassName);

		    // Invoke the supplied action listener so the calling
		    // application can update its components to the new LAF
		    // Reuse the event that was passed here.
		    listener.actionPerformed(event);
		}
	    });

	    // Only allow one menu item to be selected at once
	    radiogroup.add(item);  
	}

	return plafmenu;
    }
}
