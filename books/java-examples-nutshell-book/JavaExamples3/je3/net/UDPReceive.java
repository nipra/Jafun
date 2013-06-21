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
package je3.net;
import java.io.*;
import java.net.*;

/**
 * This program waits to receive datagrams sent the specified port.
 * When it receives one, it displays the sending host and prints the
 * contents of the datagram as a string.  Then it loops and waits again.
 **/
public class UDPReceive {
    public static final String usage = "Usage: java UDPReceive <port>";
    public static void main(String args[]) {
        try {
            if (args.length != 1) 
                throw new IllegalArgumentException("Wrong number of args");
	    
            // Get the port from the command line
            int port = Integer.parseInt(args[0]);
	    
            // Create a socket to listen on the port.
            DatagramSocket dsocket = new DatagramSocket(port);
	    
            // Create a buffer to read datagrams into.  If anyone sends us a 
            // packet containing more than will fit into this buffer, the
            // excess will simply be discarded!
            byte[] buffer = new byte[2048];
	    
	    // Create a packet to receive data into the buffer
	    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

            // Now loop forever, waiting to receive packets and printing them.
            for(;;) {
                // Wait to receive a datagram
                dsocket.receive(packet);

                // Decode the bytes of the packet to characters, using the 
		// UTF-8 encoding, and then display those characters.
                String msg = new String(buffer, 0, packet.getLength(),"UTF-8");
                System.out.println(packet.getAddress().getHostName() +
				   ": " + msg);
		
		// Reset the length of the packet before reusing it.
		// Prior to Java 1.1, we'd just create a new packet each time.
		packet.setLength(buffer.length);
            }
        }
        catch (Exception e) {
            System.err.println(e);
            System.err.println(usage);
        }
    }
}
