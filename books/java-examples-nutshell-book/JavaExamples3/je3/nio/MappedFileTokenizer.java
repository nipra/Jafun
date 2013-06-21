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

/**
 * This class implements the Tokenizer interface for a FileChannel and Charset.
 * It extends ByteBufferTokenizer and uses FileChannel.map() to memory-map the
 * contents of the file into a ByteBuffer.
 */
public class MappedFileTokenizer extends ByteBufferTokenizer {
    static final int DEFAULT_BUFFER_SIZE = 32*1024;
    FileChannel channel;    // The file we want to tokenize
    int byteBufferSize;     // What size chunks to map at at time
    long filesize;          // How big the file is
    long fileposition;      // Starting position of the next chunk

    // Construct a tokenizer for the specified FileChannel, assuming the
    // file contains text encoded using the specified Charset.
    public MappedFileTokenizer(FileChannel channel, Charset charset)
	throws IOException
    {
	this(channel, charset, DEFAULT_BUFFER_SIZE, DEFAULT_BUFFER_SIZE);
    }

    // Construct a tokenizer for the specified file and charset, additionally
    // specifying the size of the byte and characters buffers to use.
    public MappedFileTokenizer(FileChannel channel, Charset charset,
			       int charBufferSize, int byteBufferSize)
	throws IOException
    {
	super(charset, charBufferSize); // Superclass handles charset and size
	this.channel = channel;
	this.byteBufferSize = byteBufferSize; 
	filesize = channel.size();      // Get the length of the file
	fileposition = 0;               // And start at the beginning
    }

    // Return true if there are more bytes for us to return
    protected boolean hasMoreBytes() { return fileposition < filesize; }

    // Read the next chunk of bytes and return them.
    protected ByteBuffer getMoreBytes() throws IOException {
	// Return byteBufferSize bytes, or the number remaining in the file
	// if that is less
	long length = byteBufferSize;
	if (fileposition + length > filesize) length = filesize-fileposition;

	// Memory map the bytes into a buffer
	ByteBuffer buffer =
	    channel.map(FileChannel.MapMode.READ_ONLY, fileposition, length);
	// Store the position of the next chunk
	fileposition += length;
	// And return the memory-mapped buffer of bytes.
	return buffer;
    }
}
