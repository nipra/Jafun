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
import java.nio.channels.*;

/**
 * FileCopy2.java: this program copies the file named in its first argument
 * to the file named in its second argument, or to standard output if there
 * is no second argument.
 **/
public class FileCopy2 {
    public static void main(String[] args) {
	FileInputStream fin = null;   // Streams to the two files.
	FileOutputStream fout = null; // These are closed in the finally block.
	try {
	    // Open a stream to for the input file and get a channel from it
	    fin = new FileInputStream(args[0]);
	    FileChannel in = fin.getChannel();

	    // Now get the output channel
	    WritableByteChannel out;
	    if (args.length > 1) { // If there is a second filename
		fout = new FileOutputStream(args[1]);  // open file stream
		out = fout.getChannel();               // get its channel
	    }
	    else { // There is no destination filename
		out = Channels.newChannel(System.out); // wrap stdout stream
	    }

	    // Query the size of the input file
	    long numbytes = in.size();

	    // Bulk-transfer all bytes from one channel to the other.
	    // This is a special feature of FileChannel channels.
	    // See also FileChannel.transferFrom()
	    in.transferTo(0, numbytes, out); 
	}
	catch(IOException e) {
	    // IOExceptions usually have useful informative messages.
	    // Display the message if anything goes wrong.
	    System.out.println(e);
	}
	finally {
	    // Always close input and output streams.  Doing this closes
	    // the channels associated with them as well.
	    try {
		if (fin != null) fin.close();
		if (fout != null) fout.close();
	    }
	    catch(IOException e) {}
	}
    }
}
