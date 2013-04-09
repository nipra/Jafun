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
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.charset.*;
import java.util.*;           // For Set and Iterator
import javax.print.*;
import javax.print.attribute.*;

/**
 * PrintServiceWebInterface: 
 * A simple HTTP server that displays information about all accessible
 * printers on the network.
 */
public class PrintServiceWebInterface {
    public static void main(String[] args) throws IOException {
	// Get the character encoders and decoders we'll need
	Charset charset = Charset.forName("ISO-8859-1");
	CharsetEncoder encoder = charset.newEncoder();

	// The HTTP headers we send back to the client are fixed
	String headers =
	    "HTTP/1.1 200 OK\r\n" +
	    "Content-type: text/html\r\n" +
	    "Connection: close\r\n" + 
	    "\r\n";

	// We'll use two buffers in our response.  One holds the fixed
	// headers, and the other holds the variable body of the response.
	ByteBuffer[] buffers = new ByteBuffer[2];
	buffers[0] = encoder.encode(CharBuffer.wrap(headers));
	ByteBuffer body = ByteBuffer.allocateDirect(16*1024);
	buffers[1] = body;

	// Find all available PrintService objects to describe
	PrintService[] services =
	    PrintServiceLookup.lookupPrintServices(null,null);

	// All of the channels we use in this code will be in non-blocking 
	// mode. So we create a Selector object that will block while 
	// monitoring all of the channels and will only stop blocking when
	// one or more of the channels is ready for I/O of some sort.
	Selector selector = Selector.open();

	// Create a new ServerSocketChannel, and bind it to port 8000.  
	// Note that we have to do this using the underlying ServerSocket.
	ServerSocketChannel server = ServerSocketChannel.open();
	server.socket().bind(new java.net.InetSocketAddress(8000));

	// Put the ServerSocketChannel into non-blocking mode
	server.configureBlocking(false);

	// Now register the channel with the Selector.  The SelectionKey
	// represents the registration of this channel with this Selector.
	SelectionKey serverkey = server.register(selector,
						 SelectionKey.OP_ACCEPT);

	for(;;) {  // The main server loop.  The server runs forever.
	    // This call blocks until there is activity on one of the 
	    // registered channels. This is the key method in non-blocking I/O.
	    selector.select();

	    // Get a java.util.Set containing the SelectionKey objects for
	    // all channels that are ready for I/O.
	    Set keys = selector.selectedKeys();

	    // Use a java.util.Iterator to loop through the selected keys
	    for(Iterator i = keys.iterator(); i.hasNext(); ) {
		// Get the next SelectionKey in the set, and then remove it
		// from the set.  It must be removed explicitly, or it will
		// be returned again by the next call to select().
		SelectionKey key = (SelectionKey) i.next();
		i.remove();

		// Check whether this key is the SelectionKey we got when
		// we registered the ServerSocketChannel.
		if (key == serverkey) {
		    // Activity on the ServerSocketChannel means a client
		    // is trying to connect to the server.
		    if (key.isAcceptable()) {
			// Accept the client connection, and obtain a 
			// SocketChannel to communicate with the client.
			SocketChannel client = server.accept();

			// Make sure we actually got a connection
			if (client == null) continue;

			// Put the client channel in non-blocking mode.
			client.configureBlocking(false);

			// Now register the client channel with the Selector, 
			// specifying that we'd like to know when there is
			// data ready to read on the channel.
			SelectionKey clientkey =
			    client.register(selector, SelectionKey.OP_READ);
		    }
		}
		else {
		    // If the key we got from the Set of keys is not the
		    // ServerSocketChannel key, then it must be a key 
		    // representing one of the client connections.  
		    // Get the channel from the key.
		    SocketChannel client = (SocketChannel) key.channel();

		    // If we got here, it should mean that there is data to
		    // be read from the channel, but we double-check here.
		    if (!key.isReadable()) continue;
			
		    // Now read bytes from the client.  We assume that
		    // we get all the client's bytes in one read operation
		    client.read(body);

		    // The data we read should be some kind of HTTP GET 
		    // request. We don't bother checking it however since
		    // there is only one page of data we know how to return.
		    body.clear();

		    // Build an HTML document as our reponse.
		    // The body of the document contains PrintService details
		    StringBuffer response = new StringBuffer();
		    response.append(
			  "<html><head><title>Printer Status</title></head>" +
			  "<body><h1>Printer Status</h1>");
		    for(int s = 0; s < services.length; s++) {
			PrintService service = services[s];
			response.append("<h2>")
			    .append(service.getName()).append("</h2><table>");
			Attribute[] attrs = service.getAttributes().toArray();
			for(int a = 0; a < attrs.length; a++) {
			    Attribute attr = attrs[a];
			    response.append("<tr><td>").append(attr.getName()).
				append("</td><td>").append(attr).
				append("</tr>");
			}
			response.append("</table>");
		    }
		    response.append("</body></html>\r\n");

		    // Encode the response into the body ByteBuffer
		    encoder.reset();
		    encoder.encode(CharBuffer.wrap(response), body, true);
		    encoder.flush(body);

		    body.flip();   // Prepare the body buffer to be drained
		    // While there are bytes left to write
		    while(body.hasRemaining()) {
			// Write both header and body buffers
			client.write(buffers); 
		    }
		    buffers[0].flip();  // Prepare header buffer for next write
		    body.clear();       // Prepare body buffer for next read

		    // Once we've sent our response, we have no more interest
		    // in the client channel or its SelectionKey
		    client.close();  // Close the channel.
		    key.cancel();    // Tell Selector to stop monitoring it.
		}
	    }
	}
    }
}
