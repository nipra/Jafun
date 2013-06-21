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
import java.nio.channels.*;
import java.nio.charset.*;
import java.net.*;

/**
 * A simple TCP server for the daytime service.  See RFC867 for details.
 * This implementation lacks meaningful exception handling and cannot
 * handle UDP connections.
 **/
public class SimpleDaytimeServer {
    public static void main(String args[]) throws java.io.IOException {
	// RFC867 specifies port 13 for this service.  On Unix platforms,
	// you need to be running as root to use that port, so we allow
	// this service to use other ports for testing.
	int port = 13;  
	if (args.length > 0) port = Integer.parseInt(args[0]);

	// Create a channel to listen for connections on.
	ServerSocketChannel server = ServerSocketChannel.open();

	// Bind the channel to a local port.  Note that we do this by obtaining
	// the underlying java.net.ServerSocket and binding that socket.
	server.socket().bind(new InetSocketAddress(port));

	// Get an encoder for converting strings to bytes
	CharsetEncoder encoder = Charset.forName("US-ASCII").newEncoder();

	for(;;) {  // Loop forever, processing client connections
	    // Wait for a client to connect
	    SocketChannel client = server.accept();
	    
	    // Build response string, wrap, and encode to bytes
	    String date = new java.util.Date().toString() + "\r\n";
	    ByteBuffer response = encoder.encode(CharBuffer.wrap(date));

	    // Send the response to the client and disconnect.
	    client.write(response);
	    client.close();
	}
    }
}
