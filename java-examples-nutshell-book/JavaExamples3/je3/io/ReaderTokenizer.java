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
package je3.io;
import je3.classes.Tokenizer;
import je3.classes.AbstractTokenizer;
import java.io.*;

/**
 * This Tokenizer implementation extends AbstractTokenizer to tokenize a stream
 * of text read from a java.io.Reader.  It implements the createBuffer() and
 * fillBuffer() methods required by AbstractTokenizer.  See that class for
 * details on how these methods must behave.  Note that a buffer size may
 * be selected, and that this buffer size also determines the maximum token
 * length.  The Test class is a simple test that tokenizes a file and uses
 * the tokens to produce a copy of the file
 **/
public class ReaderTokenizer extends AbstractTokenizer {
    Reader in;

    // Create a ReaderTokenizer with a default buffer size of 16K characters
    public ReaderTokenizer(Reader in) { this(in, 16*1024); }

    public ReaderTokenizer(Reader in, int bufferSize) {
	this.in = in;  // Remember the reader to read input from
	// Tell our superclass about the selected buffer size.
	// The superclass will pass this number to createBuffer()
	maximumTokenLength(bufferSize);
    }

    // Create a buffer to tokenize.
    protected void createBuffer(int bufferSize) {
	// Make sure AbstractTokenizer only calls this method once
	assert text == null;
	this.text = new char[bufferSize];  // the new buffer
	this.numChars = 0;                 // how much text it contains
    }

    // Fill or refill the buffer.
    // See AbstractTokenizer.fillBuffer() for what this method must do.
    protected boolean fillBuffer() throws IOException {
	// Make sure AbstractTokenizer is upholding its end of the bargain
	assert text!=null && 0 <= tokenStart && tokenStart <= tokenEnd &&
	    tokenEnd <= p && p <= numChars && numChars <= text.length;

	// First, shift already tokenized characters out of the buffer
	if (tokenStart > 0) {
	    // Shift array contents
	    System.arraycopy(text, tokenStart, text, 0, numChars-tokenStart);
	    // And update buffer indexes
	    tokenEnd -= tokenStart; 
	    p -= tokenStart;
	    numChars -= tokenStart;
	    tokenStart = 0; 
	}

	// Now try to read more characters into the buffer
	int numread = in.read(text, numChars, text.length-numChars);
	// If there are no more characters, return false
	if (numread == -1) return false;
	// Otherwise, adjust the number of valid characters in the buffer
	numChars += numread;
	return true;  
    }

    // This test class tokenizes a file, reporting the tokens to standard out
    // and creating a copy of the file to demonstrate that every input
    // character is accounted for (since spaces are not skipped).
    public static class Test {
	public static void main(String[] args) throws java.io.IOException {
	    Reader in = new FileReader(args[0]);
	    PrintWriter out = new PrintWriter(new FileWriter(args[0]+".copy"));
	    ReaderTokenizer t = new ReaderTokenizer(in);
	    t.tokenizeWords(true).tokenizeNumbers(true).tokenizeSpaces(true);
	    while(t.next() != Tokenizer.EOF) {
		switch(t.tokenType()) {
		case Tokenizer.EOF:
		    System.out.println("EOF"); break;
		case Tokenizer.WORD:
		    System.out.println("WORD: " + t.tokenText()); break;
		case Tokenizer.NUMBER:
		    System.out.println("NUMBER: " + t.tokenText()); break;
		case Tokenizer.SPACE:
		    System.out.println("SPACE"); break;
		default:
		    System.out.println((char)t.tokenType());
		}
		out.print(t.tokenText());  // Copy token to the file
	    }
	    out.close();
	}
    }
}
