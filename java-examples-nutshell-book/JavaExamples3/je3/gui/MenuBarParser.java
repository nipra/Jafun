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
import java.util.*;

/**
 * Parse a JMenuBar from a ResourceBundle.  A menubar is represented
 * simply as a list of menu property names.  E.g.:
 *     menubar: menu.file menu.edit menu.view menu.help
 **/
public class MenuBarParser implements ResourceParser {
    static final Class[] supportedTypes = new Class[] { JMenuBar.class };
    public Class[] getResourceTypes() { return supportedTypes; }

    public Object parse(GUIResourceBundle bundle, String key, Class type)
	throws java.util.MissingResourceException
    {
	// Get the value of the key as a list of strings
	List menuList = bundle.getStringList(key);

	// Create a MenuBar
	JMenuBar menubar = new JMenuBar();

	// Create a JMenu for each of the menu property names, 
	// and add it to the bar
	int nummenus = menuList.size();
	for(int i = 0; i < nummenus; i++) {
	    menubar.add((JMenu) bundle.getResource((String)menuList.get(i),
						   JMenu.class));
	}
	
	return menubar;
    }
}
