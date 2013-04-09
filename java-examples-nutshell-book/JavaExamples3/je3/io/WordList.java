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
import java.io.*;

/**
 * This class represents a list of strings saved persistently to a file, 
 * along with an index that allows random access to any string in the list.
 * The static method writeWords() creates such an indexed list in a file.
 * The class demostrates the use of java.io.RandomAccessFile
 */
public class WordList {
    // This is a simple test method
    public static void main(String args[]) throws IOException {
	// Write command line arguments to a WordList file named "words.data"
	writeWords("words.data", args);
	
	// Now create a WordList based on that file
	WordList list = new WordList("words.data");
	// And iterate through the elements of the list backward
	// This would be very inefficient to with sequential-access streams
	for(int i = list.size()-1; i >= 0; i--) 
	    System.out.println(list.get(i));
	// Tell the list we're done with it.
	list.close();
    }

    // This static method creates a WordList file
    public static void writeWords(String filename, String[] words)
	throws IOException
    {
	// Open the file for read/write access ("rw").  We only need to write,
	// but have to request read access as well
	RandomAccessFile f = new RandomAccessFile(filename, "rw");

	// This array will hold the positions of each word in the file
	long wordPositions[] = new long[words.length];

	// Reserve space at the start of the file for the wordPositions array
	// and the length of that array. 4 bytes for length plus 8 bytes for
	// each long value in the array.
	f.seek(4L + (8 * words.length));

	// Now, loop through the words and write them out to the file,
	// recording the start position of each word.  Note that the
	// text is written in the UTF-8 encoding, which uses 1, 2, or 3 bytes
	// per character, so we can't assume that the string length equals
	// the string size on the disk.  Also note that the writeUTF() method
	// records the length of the string so it can be read by  readUTF().
	for(int i = 0; i < words.length; i++) {
	    wordPositions[i] = f.getFilePointer(); // record file position
	    f.writeUTF(words[i]);                  // write word
	}

	// Now go back to the beginning of the file and write the positions
	f.seek(0L);                                   // Start at beginning
	f.writeInt(wordPositions.length);             // Write array length
	for(int i = 0; i < wordPositions.length; i++) // Loop through array
	    f.writeLong(wordPositions[i]);              // Write array element
	f.close();   // Close the file when done.
    }

    // These are the instance fields of the WordList class
    RandomAccessFile f;  // the file to read words from
    long[] positions;    // the index that gives the position of each word

    // Create a WordList object based on the named file
    public WordList(String filename) throws IOException {
	// Open the random access file for read-only access
	f = new RandomAccessFile(filename, "r");

	// Now read the array of file positions from it
	int numwords = f.readInt();             // Read array length
	positions = new long[numwords];         // Allocate array
	for(int i = 0; i < numwords; i++)       // Read array contents
	    positions[i] = f.readLong();
    }

    // Call this method when the WordList is no longer needed.
    public void close() throws IOException {
	if (f != null) f.close();  // close file
	f = null;                  // remember that it is closed
	positions = null;
    }

    // Return the number of words in the WordList
    public int size() {
	// Make sure we haven't closed the file already
	if (f == null) throw new IllegalStateException("already closed");
	return positions.length;
    }

    // Return the string at the specified position in the WordList
    // Throws IllegalStateException if already closed, and throws
    // ArrayIndexOutOfBounds if i is negative or >= size()
    public String get(int i) throws IOException {
	// Make sure close() hasn't already been called.
	if (f == null) throw new IllegalStateException("already closed");
	f.seek(positions[i]);  // Move to the word position in the file.
	return f.readUTF();    // Read and return the string at that position.
    }
}
