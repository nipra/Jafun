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
package je3.graphics;
import java.io.*;
import java.awt.*;
import java.awt.image.*;

/*
 * This program creates PNG images of the specified color that fade from fully
 * opaque to fully transparent.  Images of this sort are useful in web design
 * where they can be used as background images and combined with background
 * colors to produce two-color fades. (IE6 does not support PNG transparency).
 * 
 * Images are produced in three sizes and with and 8 directions.  The images
 * are written into the current directory and are given names of the form:
 * fade-to-color-speed-direction.png
 * 
 *   color:      the color name specified on the command line
 *   speed:      slow (1024px), medium (512px), fast(256px)
 *   direction:  a compass point: N, E, S, W, NE, SE, SW, NW
 *
 * Invoke this program with a color name and three floating-point values 
 * specifying the red, green, and blue components of the color. 
 */
public class MakeFades  {
    // A fast fade is a small image, and a slow fade is a large image
    public static final String[] sizeNames = { "fast", "medium", "slow" };
    public static final int[] sizes = { 256, 512, 1024 };

    // Direction names and coordinates
    public static final String[] directionNames = { 
	"N", "E", "S", "W", "NE", "SE", "SW", "NW"
    };
    public static float[][] directions = {
	new float[] { 0f, 1f, 0f, 0f },  // North
	new float[] { 0f, 0f, 1f, 0f },  // East
	new float[] { 0f, 0f, 0f, 1f },  // South
	new float[] { 1f, 0f, 0f, 0f },  // West
	new float[] { 0f, 1f, 1f, 0f },  // Northeast
	new float[] { 0f, 0f, 1f, 1f },  // Southeast
	new float[] { 1f, 0f, 0f, 1f },  // Southwest
	new float[] { 1f, 1f, 0f, 0f }   // Northwest
    };


    public static void main(String[] args)
	throws IOException, NumberFormatException
    {
	// Parse the command-line arguments
	String colorname = args[0];
	float red = Float.parseFloat(args[1]);
	float green = Float.parseFloat(args[2]);
	float blue = Float.parseFloat(args[3]);

	// Create from and to colors based on those arguments
	Color from = new Color(red, green, blue, 0.0f);  // transparent 
	Color to = new Color(red, green, blue, 1.0f);    // opaque

	// Loop through the sizes and directions, and create an image for each
	for(int s = 0; s < sizes.length; s++) { 
	    for(int d = 0; d < directions.length; d++) {
		// This is the size of the image
		int size = sizes[s];

		// Create a GradientPaint for this direction and size
		Paint paint = new GradientPaint(directions[d][0]*size,
						directions[d][1]*size,
						from,
						directions[d][2]*size,
						directions[d][3]*size,
						to);

		// Start with a blank image that supports transparency
		BufferedImage image =
		    new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);

		// Now use fill the image with our color gradient
		Graphics2D g = image.createGraphics();
		g.setPaint(paint);
		g.fillRect(0, 0, size, size);

		// This is the name of the file we'll write the image to
		File file = new File("fade-to-" + 
				     colorname + "-" +
				     sizeNames[s] + "-" +
				     directionNames[d] + ".png");

		// Save the image in PNG format using the javax.imageio API
		javax.imageio.ImageIO.write(image, "png", file);

		// Show the user our progress by printing the filename
		System.out.println(file);
	    }
	}
    }
}
