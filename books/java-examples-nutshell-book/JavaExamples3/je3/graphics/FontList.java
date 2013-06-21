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
import java.applet.*;
import java.awt.*;

/**
 * An applet that displays the standard fonts and styles available in Java 1.1
 **/
public class FontList extends Applet {
    // The available font families
    String[] families = {"Serif",         // "TimesRoman" in Java 1.0
			 "SansSerif",     // "Helvetica" in Java 1.0
			 "Monospaced"};   // "Courier" in Java 1.0
    
    // The available font styles and names for each one
    int[] styles = {Font.PLAIN, Font.ITALIC, Font.BOLD, Font.ITALIC+Font.BOLD};
    String[] stylenames = {"Plain", "Italic", "Bold", "Bold Italic"};
    
    // Draw the applet.  
    public void paint(Graphics g) {
	for(int f=0; f < families.length; f++) {        // for each family
	    for(int s = 0; s < styles.length; s++) {        // for each style
		Font font = new Font(families[f],styles[s],18); // create font
		g.setFont(font);                                // set font
		String name = families[f] + " " +stylenames[s]; // create name
		g.drawString(name, 20, (f*4 + s + 1) * 20);     // display name
	    }
	}
    }
}
