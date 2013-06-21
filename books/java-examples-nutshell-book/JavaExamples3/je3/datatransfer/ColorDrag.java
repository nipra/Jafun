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
package je3.datatransfer;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/** 
 * Simple Drag-and-Drop customization: drag the foreground color from the first
 * label and drop it as the background color into the second one.  Try it also
 * using the ShowBean program to display a JColorChooser component with
 * dragEnabled=true.
 */
public class ColorDrag {
    public static void main(String args[]) {
	// Create two JLabel objects
	final JLabel label1 = new JLabel("Drag here"); 
	JLabel label2 = new JLabel("Drop here");

	// Register TransferHandler objects on them: label1 transfers its 
	// foreground color and label2 transfers its background color.
	label1.setTransferHandler(new TransferHandler("foreground"));
	label2.setTransferHandler(new TransferHandler("background"));

	// Give label1 a foreground color other than the default
	// Make label2 opaque so it displays its background color
	label1.setForeground(new Color(100, 100, 200));
	label2.setOpaque(true);

	// Now look for drag gestures over label1.  When one occurs, 
	// tell the TransferHandler to begin a drag.
	// Exercise: modify this gesture recognition so that the drag doesn't
	// begin until the mouse has moved 4 pixels.  This helps to keep
	// drags distinct from sloppy clicks.  To do this, you'll need both
	// a MouseListener and a MouseMotionListener.
	label1.addMouseMotionListener(new MouseMotionAdapter() {
		public void mouseDragged(MouseEvent e) {
		    TransferHandler handler = label1.getTransferHandler();
		    handler.exportAsDrag(label1, e, TransferHandler.COPY);
		}
	    });

	// Create a window, add the labels, and make it all visible.
	JFrame f = new JFrame("ColorDrag");
	f.getContentPane().setLayout(new FlowLayout());
	f.getContentPane().add(label1);
	f.getContentPane().add(label2);
	f.pack();
	f.setVisible(true);
    }
}
