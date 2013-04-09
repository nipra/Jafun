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
package je3.nio;
import java.io.*;
import java.nio.*;
import java.nio.charset.*;
import java.nio.channels.*;
import java.util.regex.*;

/**
 * BGrep: a regular expression search utility, like Unix grep, but
 * block-oriented instead of line-oriented.  For any match found, the
 * filename and character position within the file (note: not the line
 * number) are printed along with the text that matched.
 *
 * Usage:
 *   java je3.nio.BGrep [options] <pattern> <files>...
 *
 * Options:
 *   -e <encoding> specifies and encoding. UTF-8 is the default
 *   -i enables case-insensitive matching.  Use -s also for non-ASCII text
 *   -s enables strict (but slower) processing of non-ASCII characters
 * 
 * This program requires that each file to be searched fits into main
 * memory, and so does not work with extremely large files.
 **/
public class BGrep {
    public static void main(String[] args) {
	String encodingName = "UTF-8";  // Default to UTF-8 encoding
	int flags = Pattern.MULTILINE;  // Default regexp flags

	try { // Fatal exceptions are handled after this try block
	    // First, process any options
	    int nextarg = 0;
	    while(args[nextarg].charAt(0) == '-') { 
		String option = args[nextarg++];
		if (option.equals("-e")) {
		    encodingName = args[nextarg++];
		}
		else if (option.equals("-i")) {  // case-insensitive matching
		    flags |= Pattern.CASE_INSENSITIVE;
		}
		else if (option.equals("-s")) { // Strict Unicode processing
		    flags |= Pattern.UNICODE_CASE; // case-insensitive Unicode
		    flags |= Pattern.CANON_EQ;     // canonicalize Unicode
		}
		else {
		    System.err.println("Unknown option: " + option);
		    usage();
		}
	    }
	    
	    // Get the Charset for converting bytes to chars
	    Charset charset = Charset.forName(encodingName);

	    // Next argument must be a regexp. Compile it to a Pattern object
	    Pattern pattern = Pattern.compile(args[nextarg++], flags);

	    // Require that at least one file is specified
	    if (nextarg == args.length) usage();  

	    // Loop through each of the specified filenames
	    while(nextarg < args.length) {
		String filename = args[nextarg++];
		CharBuffer chars;  // This will hold complete text of the file
		try {  // Handle per-file errors locally
		    // Open a FileChannel to the named file
		    FileInputStream stream = new FileInputStream(filename);
		    FileChannel f = stream.getChannel();
		
		    // Memory-map the file into one big ByteBuffer.  This is
		    // easy but may be somewhat inefficient for short files.
		    ByteBuffer bytes = f.map(FileChannel.MapMode.READ_ONLY,
					     0, f.size());
		
		    // We can close the file once it is is mapped into memory.
		    // Closing the stream closes the channel, too.
		    stream.close();

		    // Decode the entire ByteBuffer into one big CharBuffer
		    chars = charset.decode(bytes);
		}
		catch(IOException e) { // File not found or other problem
		    System.err.println(e);   // Print error message
		    continue;                // and move on to the next file
		}
		
		// This is the basic regexp loop for finding all matches in a
		// CharSequence. Note that CharBuffer implements CharSequence. 
		// A Matcher holds state for a given Pattern and text.
		Matcher matcher = pattern.matcher(chars);
		while(matcher.find()) { // While there are more matches
		    // Print out details of the match
		    System.out.println(filename + ":" +       // file name
				       matcher.start()+": "+  // character pos
				       matcher.group());      // matching text
		}
	    }
	}
	// These are the things that can go wrong in the code above
	catch(UnsupportedCharsetException e) {    // Bad encoding name
	    System.err.println("Unknown encoding: " + encodingName);
	}
	catch(PatternSyntaxException e) {         // Bad pattern
	    System.err.println("Syntax error in search pattern:\n" +
			       e.getMessage());
	}
	catch(ArrayIndexOutOfBoundsException e) { // Wrong number of arguments
	    usage();
	}
    }
    
    /** A utility method to display invocation syntax and exit. */
    public static void usage() { 
	System.err.println("Usage: java BGrep [-e <encoding>] [-i] [-s]" +
			   " <pattern> <filename>...");
	System.exit(1);
    }
}
