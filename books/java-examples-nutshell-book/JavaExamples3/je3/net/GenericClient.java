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
 * This program connects to a server at a specified host and port.
 * It reads text from the console and sends it to the server.
 * It reads text from the server and sends it to the console.
 **/
public class GenericClient {
    public static void main(String[] args) throws IOException {
        try {
            // Check the number of arguments
            if (args.length != 2) 
                throw new IllegalArgumentException("Wrong number of args");
	    
            // Parse the host and port specifications
            String host = args[0];
            int port = Integer.parseInt(args[1]);
            
            // Connect to the specified host and port
            Socket s = new Socket(host, port);
	    
            // Set up streams for reading from and writing to the server.
            // The from_server stream is final for use in the inner class below
            final Reader from_server=new InputStreamReader(s.getInputStream());
            PrintWriter to_server = new PrintWriter(s.getOutputStream());
            
            // Set up streams for reading from and writing to the console
            // The to_user stream is final for use in the anonymous class below
            BufferedReader from_user = 
                new BufferedReader(new InputStreamReader(System.in));
	    // Pass true for auto-flush on println()
	    final PrintWriter to_user = new PrintWriter(System.out, true);
            
            // Tell the user that we've connected
            to_user.println("Connected to " + s.getInetAddress() +
			    ":" + s.getPort());
            
            // Create a thread that gets output from the server and displays 
            // it to the user.  We use a separate thread for this so that we
            // can receive asynchronous output
            Thread t = new Thread() {
                public void run() {
                    char[] buffer = new char[1024];
                    int chars_read;
                    try { 
			// Read characters from the server until the
			// stream closes, and write them to the console
                        while((chars_read = from_server.read(buffer)) != -1) {
			    to_user.write(buffer, 0, chars_read);
			    to_user.flush();
			}
                    }
                    catch (IOException e) { to_user.println(e); }

                    // When the server closes the connection, the loop above
                    // will end.  Tell the user what happened, and call
                    // System.exit(), causing the main thread to exit along
                    // with this one.
		    to_user.println("Connection closed by server.");
                    System.exit(0);
                }
            };
            
            // Now start the server-to-user thread
            t.start();
            
            // In parallel, read the user's input and pass it on to the server.
            String line;
            while((line = from_user.readLine()) != null) {
                to_server.print(line + "\r\n");
                to_server.flush();
            }
            
            // If the user types a Ctrl-D (Unix) or Ctrl-Z (Windows) to end
            // their input, we'll get an EOF, and the loop above will exit.
            // When this happens, we stop the server-to-user thread and close
            // the socket.

            s.close();
            to_user.println("Connection closed by client.");
	    System.exit(0);
        }
        // If anything goes wrong, print an error message
        catch (Exception e) { 
            System.err.println(e);
            System.err.println("Usage: java GenericClient <hostname> <port>");
        }
    }
}
