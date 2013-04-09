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
import java.net.*;

/**
 * Connect to a daytime server using the UDP protocol.
 * We use java.net instead of java.nio because DatagramChannel doesn't honor
 * the setSoTimeout() method on the underlying DatagramSocket
 */
public class DaytimeClient {
    public static void main(String args[]) throws java.io.IOException {
	// Figure out the host and port we're going to talk to
	String host = args[0];
	int port = 13;
	if (args.length > 1) port = Integer.parseInt(args[1]);

	// Create a socket to use
	DatagramSocket socket = new DatagramSocket();
	
	// Specify a 1-second timeout so that receive() does not block forever.
	socket.setSoTimeout(1000);

	// This buffer will hold the response.  On overflow, extra bytes are
	// discarded: there is no possibility of a buffer overflow attack here.
	byte[] buffer = new byte[512];
	DatagramPacket packet =  new DatagramPacket(buffer, buffer.length,
 				             new InetSocketAddress(host,port));

	// Try three times before giving up
	for(int i = 0; i < 3; i++) {
	    try {
		// Send an empty datagram to the specified host (and port)
		packet.setLength(0);  // make the packet empty
		socket.send(packet);  // send it out

		// Wait for a response (or timeout after 1 second)
		packet.setLength(buffer.length); // make room for the response
		socket.receive(packet);          // wait for the response

		// Decode and print the response
		System.out.print(new String(buffer, 0, packet.getLength(),
					    "US-ASCII"));
		// We were successful so break out of the retry loop
		break;  
	    }
	    catch(SocketTimeoutException e) {
		// If the receive call timed out, print error and retry
		System.out.println("No response");
	    }
	}

	// We're done with the channel now
	socket.close();
    }
}
