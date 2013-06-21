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
import java.nio.channels.*;

public class FileCopy3 {
    public static void main(String[] args) throws IOException {
	// Open file streams and get channels for them.
	ReadableByteChannel in = new FileInputStream(args[0]).getChannel();
	WritableByteChannel out;
	if (args.length > 1) out = new FileOutputStream(args[1]).getChannel();
	else out = Channels.newChannel(System.out);

	// Do the copy
	copy(in, out);
	
	// Exception handling and stream-closing code has been omitted.
    }

    // Read all available bytes from one channel and copy them to the other.
    public static void copy(ReadableByteChannel in, WritableByteChannel out)
	throws IOException 
    {
	// First, we need a buffer to hold blocks of copied bytes.
	ByteBuffer buffer = ByteBuffer.allocateDirect(32 * 1024);

	// Now loop until no more bytes to read and the buffer is empty
	while(in.read(buffer) != -1 || buffer.position() > 0) {
	    // The read() call leaves the buffer in "fill mode".  To prepare
	    // to write bytes from the bufferwe have to put it in "drain mode" 
	    // by flipping it: setting limit to position and position to zero
	    buffer.flip(); 

	    // Now write some or all of the bytes out to the output channel
	    out.write(buffer);

	    // Compact the buffer by discarding bytes that were written, 
	    // and shifting any remaining bytes.  This method also 
	    // prepares the buffer for the next call to read() by setting the
	    // position to the limit and the limit to the buffer capacity.
	    buffer.compact();
	}
    }
}
