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
import java.awt.datatransfer.*;
import java.io.*;
import java.awt.event.InputEvent;
import java.util.List;

/**
 * This TransferHandler subclass wraps another TransferHandler and delegates
 * most of its operations to the wrapped handler.  It adds the ability to
 * to drop or paste files using the predefined DataFlavor.javaFileListFlavor.
 * When a file list is pasted or dropped, it assumes the files are text, reads
 * them in order, concatenates their contents, and then passes the resulting
 * string to the wrapped handler for insertion.
 */
public class FileTransferHandler extends TransferHandler {
    TransferHandler wrappedHandler;    // The handler that we wrap
    // We use this array to test the wrapped handler
    static DataFlavor[] stringFlavorArray =
	new DataFlavor[] { DataFlavor.stringFlavor };
    
    /** Pass an existing TransferHandler to this constructor */
    public FileTransferHandler(TransferHandler wrappedHandler) {
	if (wrappedHandler == null)  	      // Fail immediately on null
	    throw new NullPointerException();
	this.wrappedHandler = wrappedHandler; // Remember wrapped handler
    }
    
    /**
     * This method returns true if the TransferHandler knows how to work
     * with one of the specified flavors.  This implementation first checks
     * the superclass, then checks for fileListFlavor support
     */
    public boolean canImport(JComponent c, DataFlavor[] flavors) {
	// If the wrapped handler can import it, we're done
	if (wrappedHandler.canImport(c, flavors)) return true;
	
	// Otherwise, if the wrapped handler can handle string imports, then 
	// see if we are being offered a list of files that we can convert
	// to a string.
	if (wrappedHandler.canImport(c, stringFlavorArray)) {
	    for(int i = 0; i < flavors.length; i++) 
		if (flavors[i].equals(DataFlavor.javaFileListFlavor))
		    return true;
	}
	
	// Otherwise, we can't import any of the flavors.
	return false;
    }
    
    /**
     * If the wrapped handler can import strings and the specified Transferable
     * can provide its data as a List of File objects, then we read the
     * files, and pass their contents as a string to the wrapped handler.
     * Otherwise, we offer the Transferable to the wrapped handler to handle
     * on its own.
     */
    public boolean importData(JComponent c, Transferable t) {
	// See if we're offered a java.util.List of java.io.File objects.
	// We handle this case first because the Transferable is likely to
	// also offer the filenames as strings, and we want to import the
	// file contents, not their names!
	if (t.isDataFlavorSupported(DataFlavor.javaFileListFlavor) &&
	    wrappedHandler.canImport(c, stringFlavorArray)) {
	    try {
		List filelist =
		    (List)t.getTransferData(DataFlavor.javaFileListFlavor);
		
		// Loop through the files to determine total size
		int numfiles = filelist.size();
		int numbytes = 0;
		for(int i = 0; i < numfiles; i++) {
		    File f = (File)filelist.get(i);
		    numbytes += (int)f.length();
		}
		
		// There will never be more characters than bytes in the files
		char[] text = new char[numbytes]; // to hold file contents
		int p = 0;         // current position in the text[] array
		
		// Loop through the files again, reading their content as text
		for(int i = 0; i < numfiles; i++) {
		    File f = (File)filelist.get(i);
		    Reader r = new BufferedReader(new FileReader(f));
		    p += r.read(text, p, (int)f.length());
		}
		
		// Convert the character array to a string and wrap it
		// in a pre-defined Transferable class for transferring strings
		StringSelection selection =
		    new StringSelection(new String(text, 0, p));

		// Ask the wrapped handler to import the string
		return wrappedHandler.importData(c, selection);
	    }
	    // If anything goes wrong, just beep to tell the user
	    catch(UnsupportedFlavorException e) {
		c.getToolkit().beep(); // audible error
		return false;          // return failure code
	    }
	    catch(IOException e) {
		c.getToolkit().beep(); // audible error
		return false;          // return failure code
	    }
	}
	    
	// Otherwise let the wrapped class handle this transferable itself
	return wrappedHandler.importData(c, t);
    }
	
    /*
     * The following methods just delegate to the wrapped TransferHandler
     */
    public void exportAsDrag(JComponent c, InputEvent e, int action) {
	wrappedHandler.exportAsDrag(c, e, action);
    }
    public void exportToClipboard(JComponent c, Clipboard clip, int action) {
	wrappedHandler.exportToClipboard(c, clip, action);
    }
    public int getSourceActions(JComponent c) {
	return wrappedHandler.getSourceActions(c);
    }
    public Icon getVisualRepresentation(Transferable t) {
	// This method is not currently (Java 1.4) used by Swing
	return wrappedHandler.getVisualRepresentation(t);
    }

    /** 
     * This class demonstrates the FileTransferHandler by installing it on a
     * JTextArea component and providing a JFileChooser to drag and cut files.
     */
    public static class Test {
	public static void main(String[] args) {
	    // Here's the text area.  Note how we wrap our TransferHandler
	    // around the default handler returned by getTransferHandler()
	    JTextArea textarea = new JTextArea();
	    TransferHandler defaultHandler = textarea.getTransferHandler();
	    textarea.setTransferHandler(new FileTransferHandler(defaultHandler));
	    // Here's a JFileChooser, with dragging explicitly enabled.
	    JFileChooser filechooser = new JFileChooser();
	    filechooser.setDragEnabled(true);

	    // Display them both in a window
	    JFrame f = new JFrame("File Transfer Handler Test");
	    f.getContentPane().add(new JScrollPane(textarea), "Center");
	    f.getContentPane().add(filechooser, "South");
	    f.setSize(400, 600);
	    f.setVisible(true);
	}
    }
}
