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
package je3.classes;
/**
 * This is a subclass of Rect that allows itself to be drawn on a screen.
 * It inherits all the fields and methods of Rect
 * It relies on the java.awt.Graphics object to perform the drawing.
 **/
public class DrawableRect extends Rect {
    /** The DrawableRect constructor just invokes the Rect() constructor */
    public DrawableRect(int x1, int y1, int x2, int y2) { super(x1,y1,x2,y2); }
    
    /** This is the new method defined by DrawableRect */
    public void draw(java.awt.Graphics g) {
        g.drawRect(x1, y1, (x2 - x1), (y2-y1));
    }
}
