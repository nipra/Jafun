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

public class ColumnLayoutPane extends JPanel {
    public ColumnLayoutPane() {
	// Specify a ColumnLayout LayoutManager, with right alignment
	this.setLayout(new ColumnLayout(5, 5, 10, ColumnLayout.RIGHT));

	// Create some buttons and set their sizes and positions explicitly
	for(int i = 0; i < 6; i++) {
	    int pointsize = 8 + i*2;
	    JButton b = new JButton("Point size " + pointsize);
	    b.setFont(new Font("helvetica", Font.BOLD, pointsize));
	    this.add(b);
	}
    }
}
