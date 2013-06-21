import java.net.*;

/*
 * A simple demonstration of setting up a Java network client.
 * @version $Id: Connect.java,v 1.3 2001/03/13 20:32:38 ian Exp $
 */ 
public class Connect {
	public static void main(String[] argv) {
		String server_name = "localhost";

		try {
			Socket sock = new Socket(server_name, 80);

			/* Finally, we can read and write on the socket. */
			System.out.println(" *** Connected to " + server_name  + " ***");
			/* ... */

			sock.close();

		} catch (java.io.IOException e) {
			System.err.println("error connecting to " + 
				server_name + ": " + e);
			return;
		}

	}
}
