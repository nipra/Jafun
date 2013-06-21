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

/**
 * Lock.java: this class demonstrates file locking and simple file read and
 * write operations using java.nio.channels.FileChannel.  It uses file locking
 * to prevent two instances of the program from running at the same time.
 */
public class Lock {
    public static void main(String args[])
	throws IOException, InterruptedException
    {
	RandomAccessFile file = null; // The file we'll lock
	FileChannel f = null;         // The channel to the file
	FileLock lock = null;         // The lock object we hold

	try {   // The finally clause closes the channel and releases the lock
	    // We use a temporary file as the lock file.
	    String tmpdir = System.getProperty("java.io.tmpdir");
	    String filename = Lock.class.getName()+ ".lock";
	    File lockfile = new File(tmpdir, filename);
	    
	    // Create a FileChannel that can read and write that file.
	    // Note that we rely on the java.io package to open the file,
	    // in read/write mode, and then just get a channel from it.
	    // This will create the file if it doesn't exit.  We'll arrange
	    // for it to be deleted below, if we succeed in locking it.
	    file = new RandomAccessFile(lockfile, "rw");
	    f = file.getChannel();
	    
	    // Try to get an exclusive lock on the file.
	    // This method will return a lock or null, but will not block.
	    // See also FileChannel.lock() for a blocking variant.
	    lock = f.tryLock();
	    
	    if (lock != null) {  
		// We obtained the lock, so arrange to delete the file when
		// we're done, and then write the approximate time at which 
		// we'll relinquish the lock into the file.
		lockfile.deleteOnExit();  // Just a temporary file

		// First, we need a buffer to hold the timestamp
		ByteBuffer bytes = ByteBuffer.allocate(8); // a long is 8 bytes

		// Put the time in the buffer and flip to prepare for writing
		// Note that many Buffer methods can be "chained" like this.
		bytes.putLong(System.currentTimeMillis() + 10000).flip();

		f.write(bytes); // Write the buffer contents to the channel
		f.force(false); // Force them out to the disk
	    }
	    else {
		// We didn't get the lock, which means another instance is
		// running.  First, let the user know this.
		System.out.println("Another instance is already running");
		
		// Next, we attempt to read the file to figure out how much
		// longer the other instance will be running.  Since we don't
		// have a lock, the read may fail or return inconsistent data.
		try {
		    ByteBuffer bytes = ByteBuffer.allocate(8);
		    f.read(bytes);  // Read 8 bytes from the file
		    bytes.flip();   // Flip buffer before extracting bytes
		    long exittime = bytes.getLong(); // Read bytes as a long
		    // Figure out how long that time is from now and round
		    // it to the nearest second.
		    long secs = (exittime-System.currentTimeMillis()+500)/1000;
		    // And tell the user about it.
		    System.out.println("Try again in about "+secs+" seconds");
		}
		catch(IOException e) {
		    // This probably means that locking is enforced by the OS
		    // and we were prevented from reading the file.
		}

		// This is an abnormal exit, so set an exit code.
		System.exit(1);
	    }
	    
	    
	    // Simulate a real application by sleeping for 10 seconds.
	    System.out.println("Starting...");
	    Thread.sleep(10000);
	    System.out.println("Exiting.");
	}
	finally {  
	    // Always release the lock and close the file
	    // Closing the RandomAccessFile also closes its FileChannel.
	    if (lock != null && lock.isValid()) lock.release();
	    if (file != null) file.close();
	}
    }
}
