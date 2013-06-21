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

public class ChannelToWriter {
    /**
     * Read bytes from the specified channel, decode them using the specified
     * Charset, and write the resulting characters to the specified writer
     */
    public static void copy(ReadableByteChannel channel, Writer writer,
			    Charset charset)
	throws IOException
    {
	// Get and configure the CharsetDecoder we'll use
	CharsetDecoder decoder = charset.newDecoder();
	decoder.onMalformedInput(CodingErrorAction.IGNORE);
	decoder.onUnmappableCharacter(CodingErrorAction.IGNORE);

	// Get the buffers we'll use, and the backing array for the CharBuffer.
	ByteBuffer bytes = ByteBuffer.allocateDirect(2*1024);
	CharBuffer chars = CharBuffer.allocate(2*1024);
	char[] array = chars.array();

	while(channel.read(bytes) != -1) { // Read from channel until EOF
	    bytes.flip();                  // Switch to drain mode for decoding
	    // Decode the byte buffer into the char buffer.
	    // Pass false to indicate that we're not done.
	    decoder.decode(bytes, chars, false);

	    // Put the char buffer into drain mode, and write its contents
	    // to the Writer, reading them from the backing array.
	    chars.flip();               
	    writer.write(array, chars.position(), chars.remaining());  

	    // Discard all bytes we decoded, and put the byte buffer back into
	    // fill mode.  Since all characters were output, clear that buffer.
	    bytes.compact();            // Discard decoded bytes
	    chars.clear();              // Clear the character buffer
	}
	    
	// At this point there may still be some bytes in the buffer to decode
	// So put the buffer into drain mode call decode() a final time, and
	// finish with a flush().
	bytes.flip();
	decoder.decode(bytes, chars, true);  // True means final call
	decoder.flush(chars);                // Flush any buffered chars
	// Write these final chars (if any) to the writer.
	chars.flip();                           
	writer.write(array, chars.position(), chars.remaining());  
	writer.flush();
    }

    // A test method: copy a UTF-8 file to standard out
    public static void main(String[] args) throws IOException {
	FileChannel c = new FileInputStream(args[0]).getChannel();
	OutputStreamWriter w = new OutputStreamWriter(System.out);
	Charset utf8 = Charset.forName("UTF-8");
	ChannelToWriter.copy(c, w, utf8);
	c.close();
	w.close();
    }
}
