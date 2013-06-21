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
import java.awt.image.*;

/**
 * This class is a Swing component that computes and displays a fractal image
 * known as a "Julia set".  The print() method demonstrates printing with the
 * Java 1.1 printing API, and is the main point of the example.  The code
 * that computes the Julia set uses complex numbers, and you don't need to 
 * understand it.
 **/
public class JuliaSet1 extends JComponent {
    // These constants are hard-coded for simplicity
    double x1=-1.5, y1=-1.5, x2=1.5, y2=1.5;  // Region of complex plane 
    int width = 400, height = 400;            // Mapped to these pixels
    double cx, cy;  // This complex constant defines the set we display
    BufferedImage image; // The image we compute
    
    // We compute values between 0 and 63 for each point in the complex plane.
    // This array holds the color values for each of those values.
    static int[] colors;
    static {  // Static initializer for the colors[] array.
	colors = new int[64];  
	for(int i = 0; i < colors.length; i++) {
	    colors[63-i] = (i*4 << 16) + (i*4 << 8) + i*4; // grayscale
	    // (i*4) ^ ((i * 3)<<6) ^ ((i * 7)<<13); // crazy technicolor
	}
    }

    // No-arg constructor with default values for cx, cy.
    public JuliaSet1() { this(-1, 0); }

    // This constructor specifies the {cx,cy} constant.
    // For simplicity, the other constants remain hardcoded.
    public JuliaSet1(double cx, double cy) {
	this.cx = cx;
	this.cy = cy;
	setPreferredSize(new Dimension(width, height));
	computeImage();
    }

    
    // This method computes a color value for each pixel of the image
    void computeImage() {
	// Create the image
	image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

	// Now loop through the pixels
	int i,j;
	double x, y;
	double dx = (x2 - x1)/width;
	double dy = (y2 - y1)/height;
	for(j = 0, y = y1; j < height; j++, y += dy) {
	    for(i = 0, x = x1; i < width; i++, x += dx) {
		// For each pixel, call testPoint() to determine a value.
		// Then map that value to a color and set it in the image.
		// If testPoint() returns 0, the point is part of the Julia set
		// and is displayed in black.  If it returns 63, the point is
		// displayed in white.  Values in-between are displayed in 
		// varying shades of gray.
		image.setRGB(i, j, colors[testPoint(x,y)]);
	    }
	}
    }

    // This is the key method for computing Julia sets.  For each point z
    // in the complex plane, we repeatedly compute z = z*z + c using complex
    // arithmetic.  We stop iterating when the magnitude of z exceeds 2 or
    // after 64 iterations.  We return the number of iterations-1.
    public int testPoint(double zx, double zy) {
	for(int i = 0; i < colors.length; i++) {
	    // Compute z = z*z + c;
	    double newx = zx*zx - zy*zy + cx;
	    double newy = 2*zx*zy + cy;
	    zx = newx;
	    zy = newy;
	    // Check magnitude of z and return iteration number
	    if (zx*zx + zy*zy > 4) return i;
	}
	return colors.length-1;
    }

    // This method overrides JComponent to display the julia set.
    // Just scale the image to fit and draw it.
    public void paintComponent(Graphics g) {
	g.drawImage(image,0,0,getWidth(), getHeight(),this);
    }

    // This method demonstrates the Java 1.1 java.awt.PrintJob printing API.
    // It also demonstrates the JobAttributes and PageAttributes classes
    // added in Java 1.3.  Display the Julia set with ShowBean and use
    // the Command menu to invoke this print command.
    public void print() {
	// Create some attributes objects.  This is Java 1.3 stuff.
	// In Java 1.1, we'd use a java.util.Preferences object instead.
	JobAttributes jattrs = new JobAttributes();
	PageAttributes pattrs = new PageAttributes();

	// Set some example attributes: monochrome, landscape mode
	pattrs.setColor(PageAttributes.ColorType.MONOCHROME);
	pattrs.setOrientationRequested(
			    PageAttributes.OrientationRequestedType.LANDSCAPE);
	// Print to file by default
	jattrs.setDestination(JobAttributes.DestinationType.FILE);
	jattrs.setFileName("juliaset.ps");

	// Look up the Frame that holds this component
	Component frame = this;
	while(!(frame instanceof Frame)) frame = frame.getParent();

	// Get a PrintJob object to print the Julia set with.
	// The getPrintJob() method displays a print dialog and allows the user
	// to override and modify the default JobAttributes and PageAttributes
	Toolkit toolkit = this.getToolkit();
	PrintJob job = toolkit.getPrintJob((Frame)frame, "JuliaSet1",
					   jattrs, pattrs);
	
	// We get a null PrintJob if the user clicked cancel
	if (job == null) return;

	// Get a Graphics object from the PrintJob.
	// We print simply by drawing to this Graphics object.
	Graphics g = job.getGraphics();

	// Center the image on the page
	Dimension pagesize = job.getPageDimension();    // how big is page?
	Dimension panesize = this.getSize();            // how big is image?
	g.translate((pagesize.width-panesize.width)/2,  // center it
		    (pagesize.height-panesize.height)/2);

	// Draw a box around the Julia Set and label it
	g.drawRect(-1, -1, panesize.width+2, panesize.height+2);
	g.drawString("Julia Set for c={" + cx + "," + cy + "}",
		     0, -15);

	// Set a clipping region
	g.setClip(0, 0, panesize.width, panesize.height);

	// Now print the component by calling its paint method
	this.paint(g);

	// Finally tell the printer we're done with the page.
	// No output will be generated if we don't call dispose() here.
	g.dispose();
    }
}
