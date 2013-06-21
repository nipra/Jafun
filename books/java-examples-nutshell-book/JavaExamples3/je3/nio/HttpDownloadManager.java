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
import java.net.*;
import java.util.*;
import java.util.logging.*;

/**
 * This class manages asynchonous HTTP GET downloads and demonstrates
 * non-blocking I/O with SocketChannel and Selector and also demonstrates
 * logging with the java.util.logging package.  This example uses a number
 * of inner classes and interfaces.
 * 
 * Call download() for each HTTP GET request you want to issue.  You may
 * optionally pass a Listener object that will be notified when the download
 * terminates or encounters an exception.  download() returns a Download object
 * which holds the downloaded bytes (including HTTP headers) and which allows
 * you to poll the Status of the download.  Call release() when there are 
 * no more downloads.
 */
public class HttpDownloadManager extends Thread {
    // An enumerated type.  Values are returned by Download.getStatus()
    public static class Status {
	// We haven't connected to the server yet
	public static final Status UNCONNECTED = new Status("Unconnected");
	// We're connected to the server, sending request or receiving response
	public static final Status CONNECTED = new Status("Connected");
	// Response has been received.  Response may have been an HTTP error
	public static final Status DONE = new Status("Done");
	// Something went wrong: bad hostname, for example.
	public static final Status ERROR = new Status("Error");

	private final String name;
	private Status(String name) { this.name = name; }
	public String toString() { return name; }
    }

    // Everything you need to know about a pending download
    public interface Download {
	public String getHost();   // Hostname we're downloading from
	public int getPort();      // Defaults to port 80
	public String getPath();   // includes query string as well
	public Status getStatus(); // Status of the download
	public byte[] getData();   // Download data, including response headers
	public int getHttpStatus();// Only call when status is DONE
    }

    // Implement this interface if you want to know when a download completes
    public interface Listener {
	public void done(Download download);
	public void error(Download download, Throwable throwable);
    }

    Selector selector;          // For multiplexing non-blocking I/O.
    ByteBuffer buffer;          // A shared buffer for downloads
    List pendingDownloads;      // Downloads that don't have a Channel yet
    boolean released = false;   // Set when the release() method is called.
    Logger log;                 // Logging output goes here

    // The HTTP protocol uses this character encoding
    static final Charset LATIN1 = Charset.forName("ISO-8859-1");

    public HttpDownloadManager(Logger log) throws IOException {
	if (log == null) log = Logger.getLogger(this.getClass().getName());
	this.log = log;
	selector = Selector.open();                  // create Selector
	buffer = ByteBuffer.allocateDirect(64*1024); // allocate buffer
	pendingDownloads = Collections.synchronizedList(new ArrayList());
	this.start();                                // start thread
    }

    // Ask the HttpDownloadManager to begin a download.  Returns a Download
    // object that can be used to poll the progress of the download.  The
    // optional Listener object will be notified of when the download completes
    // or aborts.
    public Download download(URI uri, Listener l) 
	   throws IOException
    {
	if (released)
	   throw new IllegalStateException("Can't download() after release()");

	// Get info from the URI
	String scheme = uri.getScheme();
	if (scheme == null || !scheme.equals("http"))
	    throw new IllegalArgumentException("Must use 'http:' protocol");
	String hostname = uri.getHost();
	int port = uri.getPort();
	if (port == -1) port = 80; // Use default port if none specified
	String path = uri.getRawPath();
	if (path == null || path.length() == 0) path = "/";
	String query = uri.getRawQuery();
	if (query != null) path += "?" + query;

	// Create a Download object with the pieces of the URL
	Download download = new DownloadImpl(hostname, port, path, l);

	// Add it to the list of pending downloads. This is a synchronized list
	pendingDownloads.add(download);

	// And ask the thread to stop blocking in the select() call so that
	// it will notice and process this new pending Download object.
	selector.wakeup();

	// Return the Download so that the caller can monitor it if desired.
	return download;
    }

    public void release() {
	released = true; // The thread will terminate when it notices the flag.
	try { selector.close(); } // This will wake the thread up
	catch(IOException e) {
	    log.log(Level.SEVERE, "Error closing selector", e);
	}
    }

    public void run() {
	log.info("HttpDownloadManager thread starting.");

	// The download thread runs until release() is called
	while(!released) {
	    // The thread blocks here waiting for something to happen
	    try { selector.select(); }
	    catch(IOException e) {
		// This should never happen.
		log.log(Level.SEVERE, "Error in select()", e);
		return;
	    }

	    // If release() was called, the thread should exit.
	    if (released) break;

	    // If any new Download objects are pending, deal with them first
	    if (!pendingDownloads.isEmpty()) {
		// Although pendingDownloads is a synchronized list, we still
		// need to use a synchronized block to iterate through its
		// elements to prevent a concurrent call to download().
		synchronized(pendingDownloads) {
		    Iterator iter = pendingDownloads.iterator();
		    while(iter.hasNext()) {
			// Get the pending download object from the list
			DownloadImpl download = (DownloadImpl)iter.next();
			iter.remove();  // And remove it.

			// Now begin an asynchronous connection to the 
			// specified host and port.  We don't block while
			// waiting to connect.
			SelectionKey key = null;
			SocketChannel channel = null;
			try {
			    // Open an unconnected channel
			    channel = SocketChannel.open();
			    // Put it in non-blocking mode
			    channel.configureBlocking(false);
			    // Register it with the selector, specifying that
			    // we want to know when it is ready to connect
			    // and when it is ready to read.
			    key = channel.register(selector,
						   SelectionKey.OP_READ | 
						   SelectionKey.OP_CONNECT,
						   download);
			    // Create the web server address
			    SocketAddress address = 
				new InetSocketAddress(download.host,
						      download.port);
			    // Ask the channel to start connecting
			    // Note that we don't send the HTTP request yet.
			    // We'll do that when the connection completes.
			    channel.connect(address);
			}
			catch(Exception e) {
			    handleError(download, channel, key, e);
			}
		    }
		}
	    }

	    // Now get the set of keys that are ready for connecting or reading
	    Set keys = selector.selectedKeys();
	    if (keys == null) continue; // bug workaround; should not be needed
	    // Loop through the keys in the set
	    for(Iterator i = keys.iterator(); i.hasNext(); ) {
		SelectionKey key = (SelectionKey)i.next();
		i.remove();  // Remove the key from the set before handling

		// Get the Download object we attached to the key
		DownloadImpl download = (DownloadImpl) key.attachment();
		// Get the channel associated with the key.
		SocketChannel channel = (SocketChannel)key.channel();

		try {
		    if (key.isConnectable()) {  
			// If the channel is ready to connect, complete the
			// connection and then send the HTTP GET request to it.
			if (channel.finishConnect()) {
			    download.status = Status.CONNECTED;
			    // This is the HTTP request we wend
			    String request =
				"GET " + download.path + " HTTP/1.1\r\n" +
				"Host: " + download.host + "\r\n" +
				"Connection: close\r\n" +
				"\r\n";
			    // Wrap in a CharBuffer and encode to a ByteBuffer
			    ByteBuffer requestBytes =
				LATIN1.encode(CharBuffer.wrap(request));
			    // Send the request to the server.  If the bytes
			    // aren't all written in one call, we busy loop!
			    while(requestBytes.hasRemaining())
				channel.write(requestBytes);

			    log.info("Sent HTTP request: " + download.host + 
				     ":" + download.port + ": " + request);
			}
		    }
		    if (key.isReadable()) {
			// If the key indicates that there is data to be read,
			// then read it and store it in the Download object.
			int numbytes = channel.read(buffer);
			
			// If we read some bytes, store them, otherwise
			// the download is complete and we need to note this
			if (numbytes != -1) {
			    buffer.flip();  // Prepare to drain the buffer
			    download.addData(buffer); // Store the data
			    buffer.clear(); // Prepare for another read
			    log.info("Read " + numbytes + " bytes from " +
				     download.host + ":" + download.port);
			}
			else {
			    // If there are no more bytes to read
			    key.cancel();     // We're done with the key
			    channel.close();  // And with the channel.
			    download.status = Status.DONE; 
			    if (download.listener != null)  // notify listener
				download.listener.done(download);
			    log.info("Download complete from " +
				     download.host + ":" + download.port);
			}
		    }
		}
		catch (Exception e) {
		    handleError(download, channel, key, e);
		}
	    }
	}
	log.info("HttpDownloadManager thread exiting.");
    }

    // Error handling code used by the run() method: 
    // set status, close channel, cancel key, log error, notify listener.
    void handleError(DownloadImpl download, SocketChannel channel,
		     SelectionKey key, Throwable throwable)
    {
	download.status = Status.ERROR;
	try {if (channel != null) channel.close();} catch(IOException e) {}
	if (key != null) key.cancel();
	log.log(Level.WARNING,
		"Error connecting to or downloading from " + download.host +
		":" + download.port,
		throwable);
	if (download.listener != null)
	    download.listener.error(download, throwable);
    }

    // This is the Download implementation we use internally.
    static class DownloadImpl implements Download {
	final String host;     // Final fields are immutable for thread-saftey
	final int port;
	final String path;
	final Listener listener;
	volatile Status status; // Volatile fields may be changed concurrently
	volatile byte[] data = new byte[0];

	DownloadImpl(String host, int port, String path, Listener listener) {
	    this.host = host;
	    this.port = port;
	    this.path = path;
	    this.listener = listener;
	    this.status = Status.UNCONNECTED;  // Set initial status
	}

	// These are the basic getter methods
	public String getHost() { return host; }
	public int getPort() { return port; }
	public String getPath() { return path; }
	public Status getStatus() { return status; }
	public byte[] getData() { return data; }

	/**
	 * Return the HTTP status code for the download.
	 * Throws IllegalStateException if status is not Status.DONE
	 */
	public int getHttpStatus() {
	    if (status != Status.DONE) throw new IllegalStateException();
	    // In HTTP 1.1, the return code is in ASCII bytes 10-12.
	    return
		(data[9] - '0') * 100 +
		(data[10]- '0') * 10 +
		(data[11]- '0') * 1;
	}

	// Used internally when we read more data.
	// This should use a larger buffer to prevent frequent re-allocation.
	void addData(ByteBuffer buffer) {
	    assert status == Status.CONNECTED;  // only called during download
	    int oldlen = data.length;           // How many existing bytes
	    int numbytes = buffer.remaining();  // How many new bytes
	    int newlen = oldlen + numbytes;
	    byte[] newdata = new byte[newlen];  // Create new array
	    System.arraycopy(data, 0, newdata, 0, oldlen); // Copy old bytes
	    buffer.get(newdata, oldlen, numbytes);         // Copy new bytes
	    data = newdata;                     // Save new array
	}
    }

    // This class demonstrates a simple use of HttpDownloadManager.
    public static class Test {
	static int completedDownloads = 0;

	public static void main(String args[])
	    throws IOException, URISyntaxException
	{
	    // With a -v argument, our logger will display lots of messages
	    final boolean verbose = args[0].equals("-v");
	    int firstarg = 0;
	    Logger logger = Logger.getLogger(Test.class.getName());

	    if (verbose) {
		firstarg = 1;
		logger.setLevel(Level.INFO);
	    }
	    else                       // regular output
		logger.setLevel(Level.WARNING);
	    
	    // How many URLs are on the command line?
	    final int numDownloads = args.length - firstarg;
	    // Create the download manager
    	    final HttpDownloadManager dm = new HttpDownloadManager(logger);
	    // Now loop through URLs and call download() for each one
	    // passing a listener object to receive notifications
	    for(int i = firstarg; i < args.length; i++) {
		URI uri = new URI(args[i]);
		dm.download(uri, 
		    new Listener() {
			    public void done(Download d) {
				System.err.println("DONE: " + d.getHost() +
						   ": " + d.getHttpStatus());
				// If all downloads are complete, we're done
				// with the HttpDownloadManager thread.
				if (++completedDownloads == numDownloads)
				    dm.release();
			    }
			    public void error(Download d, Throwable t) {
				System.err.println(d.getHost() + ": " + t);
				if (++completedDownloads == numDownloads)
				    dm.release();
			    }
			});
	    }
	}
    }
}
