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
import java.nio.*;
import java.nio.charset.*;
import java.io.IOException;
import je3.classes.AbstractTokenizer;

/**
 * This is an abstract Tokenizer implementation for tokenizing ByteBuffers.
 * It implements the two abstract methods of AbstractTokenizer, but defines
 * two new abstract methods that subclasses must implement.  This class 
 * provides byte-to-character decoding but leaves it up to concrete subclasses
 * to provide the ByteBuffers to decode
 */
public abstract class ByteBufferTokenizer extends AbstractTokenizer {
    CharsetDecoder decoder;   // For converting bytes to characters
    CharBuffer chars;         // The characters we're working on
    ByteBuffer bytes;         // The bytes supplied by our subclass.

    // Initialize a decoder for the specified Charset, and tell our superclass
    // how big our buffer is (and thus what size tokens we can handle).
    protected ByteBufferTokenizer(Charset charset, int charBufferSize) {
	maximumTokenLength(charBufferSize);
	decoder = charset.newDecoder();
	decoder.onMalformedInput(CodingErrorAction.IGNORE);
	decoder.onUnmappableCharacter(CodingErrorAction.IGNORE);
    }

    // Create the text[] array and set numChars. 
    // These two fields are defined by the superclass.
    // Our superclass needs characters in the text[] array.  We're going to
    // decode bytes into characters in a CharBuffer.  So we create a CharBuffer
    // that uses text[] as its backing array.
    protected void createBuffer(int bufferSize) {
	// Make sure AbstractTokenizer only calls this method once
	assert text == null;

	text = new char[bufferSize];   // Create the new buffer.
	chars = CharBuffer.wrap(text); // Wrap a char buffer around it.
	numChars = 0;                  // Say how much text it contains.
    }

    // Fill or refill the buffer.
    // See AbstractTokenizer.fillBuffer() for what this method must do.
    protected boolean fillBuffer() throws IOException {
	// Make sure AbstractTokenizer is upholding its end of the bargain
	assert text!=null && 0 <= tokenStart && tokenStart <= tokenEnd &&
	    tokenEnd <= p && p <= numChars && numChars <= text.length;

	// First, shift already tokenized characters out of the buffer
	if (tokenStart > 0) {
	    // Shift array contents in the text[] array.
	    System.arraycopy(text, tokenStart, text, 0, numChars-tokenStart);
	    // And update buffer indexes. These fields defined in superclass.
	    tokenEnd -= tokenStart; 
	    p -= tokenStart;
	    numChars -= tokenStart;
	    tokenStart = 0; 

	    // Keep the CharBuffer in sync with the changes we made above.
	    chars.position(p);
	}

	// If there is still no space in the char buffer, then we've
	// encountered a token too large for our buffer size.  
	// We could try to recover by creating a larger buffer, but
	// instead, we just throw an exception
	if (chars.remaining() == 0) 
	    throw new IOException("Token too long at " + tokenLine() + ":" +
				  tokenColumn());

	// Get more bytes if we don't have a buffer or if the buffer 
	// has been emptied
	if ((bytes == null || bytes.remaining()==0) && hasMoreBytes())
	    bytes = getMoreBytes();

	// Now that we have room in the chars buffer and data in the bytes
	// buffer, we can decode some bytes into chars
	CoderResult result = decoder.decode(bytes, chars, !hasMoreBytes());

	// Get the index of the last valid character plus one.
	numChars = chars.position();

	if (result == CoderResult.OVERFLOW) {
	    // We've filled up the char buffer.  It wasn't full before, so
	    // we know we got at least one new character.
	    return true;
	}
	else if (result == CoderResult.UNDERFLOW) {
	    // This means that we decoded all the bytes and have room left
	    // in the char buffer.  Normally, this is fine.  But there is
	    // a possibility that we didn't actually get any characters.
	    if (numChars > p) return true;
	    else { // We didn't get any new characters.  Figure out why.
		if (!hasMoreBytes()) {
		    // If there are no more bytes to read, then we're at EOF
		    return false;
		}
		else {
		    // If there are still bytes remaining to read, then 
		    // we probably got part of a multi-byte sequence, and need
		    // more bytes before we can decode a character from it.
		    // Try again (recursively) to get some more bytes.
		    return fillBuffer();
		}
	    }
	}
	else {
	    // We used CodingErrorAction.IGNORE for the CharsetDecoder, so
	    // the decoding result should always be one of the above two.
	    assert false : "Unexpected CoderResult: " + result;
	    return false;
	}
    }

    /**
     * Determine if more bytes are available.
     * @return true if and only if more bytes are avalable for reading.
     */
    protected abstract boolean hasMoreBytes();
    
    /**
     * Get a buffer of bytes for decoding and tokenizing.
     * Repeated calls to this method may create a new ByteBuffer, 
     * or may refill and return the same buffer each time.
     * @return a ByteBuffer with its position set to the first new byte, and
     *         its limit set to the index of the last new byte plus 1.
     *         The return value should never be null.  If no more bytes are
     *         available return an emtpy buffer (with limit == position).
     */
    protected abstract ByteBuffer getMoreBytes() throws IOException;
}
