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

public class GridBagLayoutPane extends JPanel {
    public GridBagLayoutPane() {
	// Create and specify a layout manager
	this.setLayout(new GridBagLayout());
	
	// Create a constraints object, and specify some default values
	GridBagConstraints c = new GridBagConstraints();
	c.fill = GridBagConstraints.BOTH; // components grow in both dimensions
	c.insets = new Insets(5,5,5,5);   // 5-pixel margins on all sides
	
	// Create and add a bunch of buttons, specifying different grid
	// position, and size for each.
	// Give the first button a resize weight of 1.0 and all others
	// a weight of 0.0.  The first button will get all extra space.
	c.gridx = 0; c.gridy = 0; c.gridwidth = 4; c.gridheight=4;
	c.weightx = c.weighty = 1.0;
	this.add(new JButton("Button #1"), c);
	
	c.gridx = 4; c.gridy = 0; c.gridwidth = 1; c.gridheight=1;
	c.weightx = c.weighty = 0.0;
	this.add(new JButton("Button #2"), c);
	
	c.gridx = 4; c.gridy = 1; c.gridwidth = 1; c.gridheight=1;
	this.add(new JButton("Button #3"), c);
	
	c.gridx = 4; c.gridy = 2; c.gridwidth = 1; c.gridheight=2;
	this.add(new JButton("Button #4"), c);
	
	c.gridx = 0; c.gridy = 4; c.gridwidth = 1; c.gridheight=1;
	this.add(new JButton("Button #5"), c);
	
	c.gridx = 2; c.gridy = 4; c.gridwidth = 1; c.gridheight=1;
	this.add(new JButton("Button #6"), c);
	
	c.gridx = 3; c.gridy = 4; c.gridwidth = 2; c.gridheight=1;
	this.add(new JButton("Button #7"), c);
	
	c.gridx = 1; c.gridy = 5; c.gridwidth = 1; c.gridheight=1;
	this.add(new JButton("Button #8"), c);
	
	c.gridx = 3; c.gridy = 5; c.gridwidth = 1; c.gridheight=1;
	this.add(new JButton("Button #9"), c);
    }
}
