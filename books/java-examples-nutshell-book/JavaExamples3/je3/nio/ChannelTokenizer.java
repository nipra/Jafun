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
import java.nio.charset.*;

public class ChannelTokenizer extends ByteBufferTokenizer {
    static final int DEFAULT_BUFFER_SIZE = 32*1024;
    ReadableByteChannel channel;  // Where the bytes come from
    ByteBuffer buffer;            // Where we put those bytes
    boolean hasMoreBytes;         // Whether there are any more

    // Construct a ChannelTokenizer to tokenize the specified channel, 
    // decoding its bytes using the specified charset.
    public ChannelTokenizer(ReadableByteChannel channel, Charset charset) {
	this(channel, charset, DEFAULT_BUFFER_SIZE, DEFAULT_BUFFER_SIZE);
    }

    // Construct a ChannelTokenizer for the channel and charset, additionally
    // specifying the character and byte buffer sizes to use.
    public ChannelTokenizer(ReadableByteChannel channel, Charset charset,
			    int charBufferSize, int byteBufferSize)
    {
	super(charset, charBufferSize); // Superclass handles charset and size
	this.channel = channel;         // Remember the channel
	this.hasMoreBytes = true;       // Assume some bytes in the channel
	// Allocate the buffer we'll use to store bytes
	buffer = ByteBuffer.allocateDirect(byteBufferSize);
    }

    // Return false when we're at EOF and have returned all bytes.
    protected boolean hasMoreBytes() { return hasMoreBytes; }

    // Refill the buffer and return it
    protected ByteBuffer getMoreBytes() throws IOException {
	buffer.clear();  // Clear the buffer; prepare to fill it.
	// Read a chunk of bytes
	int bytesRead = channel.read(buffer);
	// If we are at EOF, remember that for hasMoreBytes()
	if (bytesRead == -1) hasMoreBytes = false;
	// Prepare the buffer to be drained and return it
	buffer.flip();   // Set limit to position, and position to 0
	return buffer;   // And return it.
    }
}
