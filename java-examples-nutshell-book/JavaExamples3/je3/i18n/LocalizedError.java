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
package je3.i18n;
import java.text.*;
import java.io.*;
import java.util.*;

/**
 * A convenience class that can display a localized exception message
 * depending on the class of the exception.  It uses a MessageFormat,
 * and passes five arguments that the localized message may include:
 *   {0}: the message included in the exception or error.
 *   {1}: the full class name of the exception or error.
 *   {2}: the file the exception occurred in
 *   {3}: a line number in that file.
 *   {4}: the current date and time.
 * Messages are looked up in a ResourceBundle with the basename
 * "Errors", using a the full class name of the exception object as
 * the resource name.  If no resource is found for a given exception
 * class, the superclasses are checked.
 **/
public class LocalizedError {
    public static void display(Throwable error) {
        ResourceBundle bundle;
        // Try to get the resource bundle.
        // If none, print the error in a non-localized way.
        try {
	    String bundleName = "com.davidflanagan.examples.i18n.Errors";
	    bundle = ResourceBundle.getBundle(bundleName);
	}
        catch (MissingResourceException e) {
            error.printStackTrace(System.err);
            return;
        }
	
        // Look up a localized message resource in that bundle, using the
        // classname of the error (or its superclasses) as the resource name.
        // If no resource was found, display the error without localization.
        String message = null;
        Class c = error.getClass();
        while((message == null) && (c != Object.class)) {
            try { message = bundle.getString(c.getName()); }
            catch (MissingResourceException e) { c = c.getSuperclass(); }
        }
        if (message == null) { error.printStackTrace(System.err);  return; }
	
	// Get the filename and linenumber for the exception
	// In Java 1.4, this is easy, but in prior releases, we had to try
	// parsing the output Throwable.printStackTrace();
	StackTraceElement frame = error.getStackTrace()[0];  // Java 1.4
	String filename = frame.getFileName();
	int linenum = frame.getLineNumber();

        // Set up an array of arguments to use with the message
        String errmsg = error.getMessage();
        Object[] args = {
            ((errmsg!= null)?errmsg:""), error.getClass().getName(),
            filename, new Integer(linenum), new Date()
        };

        // Finally, display the localized error message, using
        // MessageFormat.format() to substitute the arguments into the message.
        System.err.println(MessageFormat.format(message, args));
    }

    /** 
     * This is a simple test program that demonstrates the display() method.
     * You can use it to generate and display a FileNotFoundException or an
     * ArrayIndexOutOfBoundsException
     **/
    public static void main(String[] args) {
	try { FileReader in = new FileReader(args[0]); }
	catch(Exception e) { LocalizedError.display(e);	}
    }
}
