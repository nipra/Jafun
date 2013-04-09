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
package je3.print;
import javax.swing.*;
import java.awt.*;
import java.awt.print.*; // This package is the Java 1.2 printing API

/**
 * This class extends JuliaSet1 and overrides the print() method to demonstrate
 * the Java 1.2 printing API.
 */
public class JuliaSet2 extends JuliaSet1 {
    public JuliaSet2() { this(.4, .4); }  // Display a different set by default
    public JuliaSet2(double cx, double cy) { super(cx,cy); }

    // This method demonstrates the Java 1.2 printing API.
    // Test it using the ShowBean program.
    public void print() {
	// Java 1.1 used java.awt.PrintJob.
	// In Java 1.2 we use java.awt.print.PrinterJob
	PrinterJob job = PrinterJob.getPrinterJob();

	// Alter the default page settings to request landscape mode
	PageFormat page = job.defaultPage();
	page.setOrientation(PageFormat.LANDSCAPE);  // landscape by default

	// Tell the PrinterJob what Printable object we want to print.
	// PrintableComponent is defined as an inner class below
	String title = "Julia set for c={" + cx + "," + cy + "}";
	Printable printable = new PrintableComponent(this, title);
	job.setPrintable(printable, page);

	// Call the printDialog() method to give the user a chance to alter
	// the printing attributes or to cancel the printing request.
	if (job.printDialog()) {
	    // If we get here, then the user did not cancel the print job
	    // So start printing, displaying a dialog for errors.
	    try { job.print(); }
	    catch(PrinterException e) {
		JOptionPane.showMessageDialog(this, e.toString(),
					      "PrinterException",
					      JOptionPane.ERROR_MESSAGE);
	    }
	}
    }

    // This inner class implements the Printable interface for an AWT component
    public static class PrintableComponent implements Printable {
	Component c;
	String title;
	public PrintableComponent(Component c, String title) {
	    this.c = c;
	    this.title = title;
	}

	// This method should print the specified page number to the specified
	// Graphics object, abiding by the specified page format.
	// The printing system will call this method repeatedly to print all
	// pages of the print job.  If pagenum is greater than the last page,
	// it should return NO_SUCH_PAGE to indicate that it is done.  The
	// printing system may call this method multiple times per page.
	public int print(Graphics g, PageFormat format, int pagenum) {
	    // This implemenation is always a single page
	    if (pagenum > 0) return Printable.NO_SUCH_PAGE;

	    // The Java 1.2 printing API passes us a Graphics object, but we
	    // can always cast it to a Graphics2D object
	    Graphics2D g2 = (Graphics2D) g;

	    // Translate to accomodate the requested top and left margins.
	    g2.translate(format.getImageableX(), format.getImageableY());

	    // Figure out how big the drawing is, and how big the page 
	    // (excluding margins) is
	    Dimension size = c.getSize();                  // component size
	    double pageWidth = format.getImageableWidth();    // Page width
	    double pageHeight = format.getImageableHeight();  // Page height

	    // If the component is too wide or tall for the page, scale it down
	    if (size.width > pageWidth) {
		double factor = pageWidth/size.width; // How much to scale
		g2.scale(factor, factor);           // Adjust coordinate system
		pageWidth /= factor;                // Adjust page size up
		pageHeight /= factor;
	    }
	    if (size.height > pageHeight) {   // Do the same thing for height
		double factor = pageHeight/size.height;
		g2.scale(factor, factor);
		pageWidth /= factor;
		pageHeight /= factor;
	    }

	    // Now we know the component will fit on the page.  Center it by
	    // translating as necessary.
	    g2.translate((pageWidth-size.width)/2,(pageHeight-size.height)/2);

	    // Draw a line around the outside of the drawing area and label it
	    g2.drawRect(-1, -1, size.width+2, size.height+2);
	    g2.drawString(title, 0, -15);

	    // Set a clipping region so the component can't draw outside of
	    // its won bounds.
	    g2.setClip(0, 0, size.width, size.height);

	    // Finally, print the component by calling its paint() method.
	    // This prints the background, border, and children as well.
	    // For swing components, if you don't want the background, border,
	    // and children, then call printComponent() instead.
	    c.paint(g);

	    // Tell the PrinterJob that the page number was valid
	    return Printable.PAGE_EXISTS;
	}
    }
}

