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
import javax.swing.*;

public class BorderLayoutPane extends JPanel {
    String[] borders = {
	BorderLayout.NORTH, BorderLayout.EAST, BorderLayout.SOUTH,
	BorderLayout.WEST, BorderLayout.CENTER
    };
    public BorderLayoutPane() {
	// Use a BorderLayout with 10-pixel margins between components
	this.setLayout(new BorderLayout(10, 10));
	for(int i = 0; i < 5; i++) {          // Add children to the pane
	    this.add(new JButton(borders[i]),    // Add this component
		     borders[i]);                // Using this constraint
	}
    }
}
