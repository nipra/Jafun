import java.net.*;
import java.io.*;

public class HelloServer {

  public static void main(String[] args) throws IOException {
  
    int port = 2345;
    ServerSocket ss = new ServerSocket(port);
    while (true) {
      try {
        Socket s = ss.accept();
          
        String response = "Hello " + s.getInetAddress() + " on port " 
         + s.getPort() + "\r\n";
        response += "This is " + s.getLocalAddress() + " on port " 
         + s.getLocalPort() + "\r\n";
        OutputStream out = s.getOutputStream();
        out.write(response.getBytes("US-ASCII"));
        out.flush();
        s.close();
      }
      catch (IOException ex) {
        // This is an error on one connection. Maybe the client crashed.
        // Maybe it broke the connection prematurely. Whatever happened, 
        // it’s not worth shutting down the server for. 
      }
    }  // end while
  } // end main
} // end HelloServer
