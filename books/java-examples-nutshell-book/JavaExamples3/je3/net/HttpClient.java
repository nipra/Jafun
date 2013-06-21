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
import javax.net.*;
import javax.net.ssl.*;
import java.security.cert.*;

/**
 * This program connects to a Web server and downloads the specified URL
 * from it.  It uses the HTTP protocol directly and can also handle HTTPS
 **/
public class HttpClient {
    public static void main(String[] args) {
        try {
            // Check the arguments
            if (args.length != 2)
                throw new IllegalArgumentException("Wrong number of args");
            
            // Get an output stream to write the URL contents to
	    OutputStream to_file = new FileOutputStream(args[1]);
            
            // Now use the URI class to parse the user-specified URL into
            // its various parts.  java.net.URI is new in Java 1.4; it is like
	    // URL, but has more powerful parsing, and does not have built-in
	    // networking capability.
            URI uri = new URI(args[0]);
            String protocol = uri.getScheme();
            String host = uri.getHost();
            int port = uri.getPort();
	    String path = uri.getRawPath();
	    if (path == null || path.length() == 0) path = "/";
	    String query = uri.getRawQuery();
	    if (query != null && query.length() > 0)
		path += "?" + query;

	    Socket socket; // The socket we'll use to communicate 

	    if (protocol.equals("http")) {
		// This is a normal http protocol, create a normal socket
		if (port == -1) port = 80;  // Default http port
		socket = new Socket(host, port);
	    }
	    else if (protocol.equals("https")) {
		// For HTTPS we need to create a secure socket
		if (port == -1) port = 443;
		SocketFactory factory = SSLSocketFactory.getDefault();
		SSLSocket ssock = (SSLSocket) factory.createSocket(host, port);
		
		// Get the server's certificate 
		SSLSession session = ssock.getSession();
		X509Certificate cert = null;
		try {
		    cert = (X509Certificate)session.getPeerCertificates()[0];
		}
		catch(SSLPeerUnverifiedException e) { 
		    // This means there was no certificate, or the certificate
		    // was not valid.
		    System.err.println(session.getPeerHost() + 
				       " did not present a valid certificate");
		    System.exit(1);
		}

		// Print certificate details
		System.out.println(session.getPeerHost() + 
			   " has presented a certificate belonging to:\t" +
			   "[" + cert.getSubjectDN() + "]\n" +
			   "The certificate was issued by: \t" +
			   "[" + cert.getIssuerDN() + "]");	   

		// We could ask the user here to confirm that they trust
		// the certificate owner and issuer before proceeding...
		socket = ssock;
	    }
	    else {
		throw new IllegalArgumentException("URL must use http: or " +
						   "https: protocol");
	    }

	    /* 
	     * We now have a regular socket or an SSL socket.  HTTP and HTTPS
	     * are the same from here on.
	     */ 

            // Get input and output streams for the socket
	    InputStream from_server = socket.getInputStream();
	    PrintWriter to_server = new PrintWriter(socket.getOutputStream());
            
            // Send the HTTP GET command to the Web server, specifying the file
	    // We specify HTTP 1.0 instead of 1.1 because we don't know how
	    // to handle Transfer-Encoding: chunked in the response
            to_server.print("GET " + path + " HTTP/1.0\r\n" +
			    "Host: " + host + "\r\n" +
			    "Connection: close\r\n\r\n");
            to_server.flush();  // Send it right now!
            
	    // Here is a buffer we use for reading from the server
            byte[] buffer = new byte[8 * 1024];
            int bytes_read;

            // Now read the HTTP headers the server returns, and print these
	    // to the console.  Read from the server until we've got at
	    // least 4K bytes or until we get EOF.  Assume that we'll find
	    // the end of the headers somewhere in the first 4K bytes.
	    int numbytes = 0; 
	    while(true) {
		bytes_read = from_server.read(buffer, numbytes,
					      buffer.length-numbytes);
		if (bytes_read == -1) break;
		numbytes += bytes_read;
		if (numbytes >= 4 * 1024) break;
	    }

	    // Loop through the bytes, looking for the \r\n\r\n pattern
	    // (13, 10, 13, 10) that marks the end of the headers
	    int i = 0;
	    while(i <= numbytes-4) {
		if (buffer[i++] == 13 && buffer[i++] == 10 &&
		    buffer[i++] == 13 && buffer[i++] == 10) break;
	    }
	    // If we didn't find the end of the headers, abort
	    if (i > numbytes-4) {
		throw new IOException("End of headers not found in first " +
				      numbytes + " bytes");
	    }

	    // Now convert the headers to a Latin-1 string (omitting the final
	    // blank line) and then print them out to the console
	    String headers = new String(buffer, 0, i-2, "ISO-8859-1");
	    System.out.print(headers);

	    // Any bytes we read after the headers get written to the file.
	    to_file.write(buffer, i, numbytes-i);

	    // Now read the rest of the bytes and write to the file
            while((bytes_read = from_server.read(buffer)) != -1)
                to_file.write(buffer, 0, bytes_read);
            
            // When the server closes the connection, we close our stuff, too
            socket.close();
            to_file.close();
        }
        catch (Exception e) {    // Report any errors that arise
            System.err.println(e);
            System.err.println("Usage: java HttpClient <URL> [<filename>]");
        }
    }
}
