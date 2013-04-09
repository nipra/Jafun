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
import java.awt.*;

/**
 * This interface defines the methods that must be implemented by an
 * object that is to be displayed by the GraphicsExampleFrame object
 */
public interface GraphicsExample {
    public String getName();                      // Return the example name
    public int getWidth();                        // Return its width
    public int getHeight();                       // Return its height
    public void draw(Graphics2D g, Component c);  // Draw the example
}
