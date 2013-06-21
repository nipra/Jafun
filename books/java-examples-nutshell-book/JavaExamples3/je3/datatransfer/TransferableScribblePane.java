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
import java.awt.*;                  // Graphics, Rectangle, Stroke, etc.
import java.awt.datatransfer.*;     // Transferable, DataFlavor
import java.awt.dnd.*;              // Drag-and-drop listeners and events
import java.awt.event.*;            // Mouse events
import javax.swing.*;               // JComponent
import javax.swing.border.*;        // LineBorder and BevelBorder
import java.util.*;                 // ArrayList, etc.
import java.util.List;    // Explicit import to disambiguate from java.awt.List
import je3.graphics.PolyLine; // The Shape of scribbles

/**
 * This rewrite of ScribblePane allows individual PolyLine lines to be
 * selected, cut, copied, pasted, dragged, and dropped.
 */
public class TransferableScribblePane extends JComponent {
    List lines;             // The PolyLines that comprise this scribble
    PolyLine currentLine;   // The line currently being drawn
    PolyLine selectedLine;  // The line that is current selected 
    boolean canDragImage;   // Can we drag an image of the line?

    // Lines are 3 pixels wide, and the selected line is drawn dashed
    static Stroke stroke = new BasicStroke(3.0f);
    static Stroke selectedStroke = new BasicStroke(3, BasicStroke.CAP_BUTT,
						   BasicStroke.JOIN_ROUND, 0f, 
						   new float[] { 3f, 3f, },0f);

    // Different borders indicate receptivity to drops
    static Border normalBorder = new LineBorder(Color.black, 3);
    static Border canDropBorder = new BevelBorder(BevelBorder.LOWERED);

    // The constructor method
    public TransferableScribblePane() {
	setPreferredSize(new Dimension(450,200)); // We need a default size
	setBorder(normalBorder);                  // and a border.
	lines = new ArrayList();  // Start with an empty list of lines

	// Register interest in mouse button and mouse motion events.
	enableEvents(AWTEvent.MOUSE_EVENT_MASK |
		     AWTEvent.MOUSE_MOTION_EVENT_MASK);

	// Enable drag-and-drop by specifying a listener that will be 
	// notified when a drag begins.  dragGestureListener is defined later.
	DragSource dragSource = DragSource.getDefaultDragSource();
	dragSource.createDefaultDragGestureRecognizer(this,
				     DnDConstants.ACTION_COPY_OR_MOVE,
						      dragGestureListener);

	// Enable drops on this component by registering a listener to 
	// be notified when something is dragged or dropped over us.
	this.setDropTarget(new DropTarget(this, dropTargetListener));

	// Check whether the system allows us to drag an image of the line
	canDragImage = dragSource.isDragImageSupported();
    }

    /** We override this method to draw ourselves. */
    public void paintComponent(Graphics g) {
	// Let the superclass do its painting first
	super.paintComponent(g);

	// Make a copy of the Graphics context so we can modify it
	Graphics2D g2 = (Graphics2D) (g.create()); 

	// Our superclass doesn't paint the background, so do this ourselves.
	g2.setColor(getBackground());
	g2.fillRect(0, 0, getWidth(), getHeight());

	// Set the line width and color to use for the foreground
	g2.setStroke(stroke);
	g2.setColor(this.getForeground());

	// Now loop through the PolyLine shapes and draw them all
	int numlines = lines.size();
	for(int i = 0; i < numlines; i++) {
	    PolyLine line = (PolyLine)lines.get(i);
	    if (line == selectedLine) {        // If it is the selected line
		g2.setStroke(selectedStroke);  // Set dash pattern
		g2.draw(line);                 // Draw the line
		g2.setStroke(stroke);          // Revert to solid lines
	    }
	    else g2.draw(line);  // Otherwise just draw the line
	}
    }

    /**
     * This method is called on mouse button events.  It begins a new line
     * or tries to select an existing line.
     */
    public void processMouseEvent(MouseEvent e) {
	if (e.getButton() == MouseEvent.BUTTON1) {         // Left mouse button
	    if (e.getID() == MouseEvent.MOUSE_PRESSED) {   // Pressed down 
		if (e.isShiftDown()) {                     // with Shift key
		    // If the shift key is down, try to select a line
		    int x = e.getX();
		    int y = e.getY();
		
		    // Loop through the lines checking to see if we hit one
		    PolyLine selection = null;
		    int numlines = lines.size();
		    for(int i = 0; i < numlines; i++) {
			PolyLine line = (PolyLine)lines.get(i);
			if (line.intersects(x-2, y-2, 4, 4)) {
			    selection = line;
			    e.consume();
			    break;
			}
		    }
		    // If we found an intersecting line, save it and repaint
		    if (selection != selectedLine) { // If selection changed
			selectedLine = selection; // remember which is selected
			repaint();                // will make selection dashed
		    }
		}
		else if (!e.isControlDown()) {   // no shift key or ctrl key
		    // Start a new line on mouse down without shift or ctrl
		    currentLine = new PolyLine(e.getX(), e.getY());
		    lines.add(currentLine);
		    e.consume();
		}
	    }
	    else if (e.getID() == MouseEvent.MOUSE_RELEASED) {// Left Button Up
		// End the line on mouse up
		if (currentLine != null) {
		    currentLine = null;
		    e.consume();
		}
	    }
	}

	// The superclass method dispatches to registered event listeners
	super.processMouseEvent(e);
    }

    /**
     * This method is called for mouse motion events.
     * We don't have to detect gestures that initiate a drag in this method.
     * That is the job of the DragGestureRecognizer we created in the 
     * constructor: it will notify the DragGestureListener defined below.
     */
    public void processMouseMotionEvent(MouseEvent e) {
	if (e.getID() == MouseEvent.MOUSE_DRAGGED &&     // If we're dragging
	    currentLine != null) {                       // and a line exists
	    currentLine.addSegment(e.getX(), e.getY());  // Add a line segment
	    e.consume();                                 // Eat the event
	    repaint();                                   // Redisplay all lines
	}
	super.processMouseMotionEvent(e); // Invoke any listeners
    }

    /** Copy the selected line to the clipboard, then delete it */
    public void cut() {
	if (selectedLine == null) return; // Only works if a line is selected
	copy();                           // Do a Copy operation...
	lines.remove(selectedLine);       // and then erase the selected line
	selectedLine = null;
	repaint();                        // Repaint because a line was removed
    }

    /** Copy the selected line to the clipboard */
    public void copy() {
	if (selectedLine == null) return; // Only works if a line is selected
	// Get the system Clipboard object.
	Clipboard c = this.getToolkit().getSystemClipboard();

	// Wrap the selected line in a TransferablePolyLine object
	// and pass it to the clipboard, with an object to receive notification
	// when some other application takes ownership of the clipboard
	c.setContents(new TransferablePolyLine((PolyLine)selectedLine.clone()),
		      new ClipboardOwner() {
			 public void lostOwnership(Clipboard c,Transferable t){
			     // This method is called when something else
			     // is copied to the clipboard.  We could use it
			     // to deselect the selected line, if we wanted.
			 }
		      });
    }

    /** Get a PolyLine from the clipboard, if one exists, and display it */
    public void paste() {
	// Get the system Clipboard and ask for its Transferable contents
	Clipboard c = this.getToolkit().getSystemClipboard(); 
	Transferable t = c.getContents(this);

	// See if we can extract a PolyLine from the Transferable object
	PolyLine line;
	try {
	    line = (PolyLine)t.getTransferData(TransferablePolyLine.FLAVOR);
	}
	catch(Exception e) {  // UnsupportedFlavorException or IOException
	    // If we get here, the clipboard doesn't hold a PolyLine we can use
	    getToolkit().beep();   // So beep to indicate the error
	    return;
	}
	
	lines.add(line); // We got a line from the clipboard, so add it to list
	repaint();       // And repaint to make the line appear
    }

    /** Erase all lines and repaint. */
    public void clear() {
	lines.clear();
	repaint();
    }

    /**
     * This DragGestureListener is notified when the user initiates a drag.
     * We passed it to the DragGestureRecognizer we created in the constructor.
     */
    public DragGestureListener dragGestureListener = new DragGestureListener(){
	    public void dragGestureRecognized(DragGestureEvent e) {
		// Don't start a drag if there isn't a selected line
		if (selectedLine == null) return;

		// Find out where the drag began
		MouseEvent trigger = (MouseEvent)e.getTriggerEvent();
		int x = trigger.getX();
		int y = trigger.getY();

		// Don't do anything if the drag was not near the selected line
		if (!selectedLine.intersects(x-4, y-4, 8, 8)) return;

		// Make a copy of the selected line, adjust the copy so that
		// the point under the mouse is (0,0), and wrap the copy in a 
		// Tranferable wrapper.
		PolyLine copy = (PolyLine)selectedLine.clone();
		copy.translate(-x, -y);
		Transferable t = new TransferablePolyLine(copy);
		
		// If the system allows custom images to be dragged, make
		// an image of the line on a transparent background
		Image dragImage = null;
		Point hotspot = null;
		if (canDragImage) {
		    Rectangle box = copy.getBounds();
		    dragImage = createImage(box.width, box.height);
		    Graphics2D g = (Graphics2D)dragImage.getGraphics();
		    g.setColor(new Color(0,0,0,0));  // transparent bg
		    g.fillRect(0, 0, box.width, box.height);
		    g.setColor(getForeground());
		    g.setStroke(selectedStroke);
		    g.translate(-box.x, -box.y);
		    g.draw(copy);
		    hotspot = new Point(-box.x, -box.y);
		    
		}
		
		// Now begin dragging the line, specifying the listener
		// object to receive notifications about the progress of
		// the operation.  Note: the startDrag() method is defined by
		// the event object, which is unusual.
		e.startDrag(null,       // Use default drag-and-drop cursors
			    dragImage,  // Use the image, if supported
			    hotspot,    // Ditto for the image hotspot
			    t,          // Drag this object
			    dragSourceListener); // Send notifications here
	    }
	};

    /**
     * If this component is the source of a drag, then this DragSourceListener
     * will receive notifications about the progress of the drag.  The only
     * one we use here is dragDropEnd() which is called after a drop occurs.
     * We could use the other methods to change cursors or perform other
     * "drag over effects"
     */
    public DragSourceListener dragSourceListener = new DragSourceListener() {
	    // Invoked when dragging stops
	    public void dragDropEnd(DragSourceDropEvent e) {
		if (!e.getDropSuccess()) return; // Ignore failed drops
		// If the drop was a move, then delete the selected line
		if (e.getDropAction() == DnDConstants.ACTION_MOVE) {
		    lines.remove(selectedLine);
		    selectedLine = null;
		    repaint();
		}
	    }
	    // The following methods are unused here.  We could implement them
	    // to change custom cursors or perform other "drag over effects".
	    public void dragEnter(DragSourceDragEvent e) {}
	    public void dragExit(DragSourceEvent e) {}
	    public void dragOver(DragSourceDragEvent e) {}
	    public void dropActionChanged(DragSourceDragEvent e) {}
	};

    /**
     * This DropTargetListener is notified when something is dragged over
     * this component.
     */
    public DropTargetListener dropTargetListener = new DropTargetListener() {
	    // This method is called when something is dragged over us.
	    // If we understand what is being dragged, then tell the system
	    // we can accept it, and change our border to provide extra
	    // "drag under" visual feedback to the user to indicate our
	    // receptivity to a drop.
	    public void dragEnter(DropTargetDragEvent e) {
		if (e.isDataFlavorSupported(TransferablePolyLine.FLAVOR)) {
		    e.acceptDrag(e.getDropAction());
		    setBorder(canDropBorder);
		}
	    }

	    // Revert to our normal border if the drag moves off us.
	    public void dragExit(DropTargetEvent e) { setBorder(normalBorder); }
	    // This method is called when something is dropped on us.
	    public void drop(DropTargetDropEvent e) {
		// If a PolyLine is dropped, accept either a COPY or a MOVE
		if (e.isDataFlavorSupported(TransferablePolyLine.FLAVOR))
		    e.acceptDrop(e.getDropAction());
		else {  // Otherwise, reject the drop and return
		    e.rejectDrop();
		    return;
		}

		// Get the dropped object and extract a PolyLine from it
		Transferable t = e.getTransferable();
		PolyLine line;
		try {
		    line =
		       (PolyLine)t.getTransferData(TransferablePolyLine.FLAVOR);
		}
		catch(Exception ex) {  // UnsupportedFlavor or IOException
		    getToolkit().beep();   // Something went wrong, so beep
		    e.dropComplete(false); // Tell the system we failed
		    return;
		}

		// Figure out where the drop occurred, and translate so the
		// point that was formerly (0,0) is now at that point.
		Point p = e.getLocation();
		line.translate((float)p.getX(), (float)p.getY());

		// Add the line to our list, and repaint 
		lines.add(line);
		repaint();

		// Tell the system that we successfully completed the transfer.
		// This means it is safe for the initiating component to delete
		// its copy of the line
		e.dropComplete(true);
	    }

	    // We could provide additional drag under effects with this method.
	    public void dragOver(DropTargetDragEvent e) {}

	    // If we used custom cursors, we would update them here.
	    public void dropActionChanged(DropTargetDragEvent e) {} 
	};
}
