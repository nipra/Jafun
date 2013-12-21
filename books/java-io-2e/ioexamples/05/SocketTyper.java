import java.net.*;
import java.io.*;

public class SocketTyper {

  public static void main(String[] args) throws IOException {

    if (args.length != 1) {
      System.err.println("Usage: java SocketTyper url1");
      return;
    }

    URL u = new URL(args[0]);
    if (!u.getProtocol().equalsIgnoreCase("http")) {
      System.err.println("Sorry, " + u.getProtocol()
        + " is not supported");
      return;
    }
    
    String host = u.getHost();
    int port    = u.getPort();
    String file = u.getFile();
    if (file == null) file = "/";
    // default port
    if (port <= 0) port = 80;
    
    Socket s = null;
    try {
      s = new Socket(host, port);
      String request = "GET " + file + " HTTP/1.1\r\n"
       + "User-Agent: SocketTyper\r\n"
       + "Accept: text/*\r\n"
       + "Host: " + host + "\r\n"
       + "\r\n";
      byte[] b = request.getBytes("US-ASCII");
    
      OutputStream out = s.getOutputStream();
      InputStream in = s.getInputStream();
      out.write(b);
      out.flush();
    
      for (int c = in.read(); c != -1; c = in.read()) {
        System.out.write(c);
      }
    }
    finally {
      if (s != null && s.isConnected()) s.close();
    }
  }
}
