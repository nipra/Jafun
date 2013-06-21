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
package je3.applet;
import java.applet.*;   // Don't forget this import statement!
import java.awt.*;      // Or this one for the graphics!

/** This applet just says "Hello World! */
public class FirstApplet extends Applet {
    // This method displays the applet.
    public void paint(Graphics g) {
        g.drawString("Hello World", 25, 50);
    }
}
