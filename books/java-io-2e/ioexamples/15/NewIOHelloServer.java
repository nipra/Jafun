import java.net.*;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.*;

public class NewIOHelloServer {

  public final static int PORT = 2345;

  public static void main(String[] args) throws IOException {
    
    ServerSocketChannel serverChannel = ServerSocketChannel.open();
    SocketAddress port = new InetSocketAddress(PORT);
    serverChannel.socket().bind(port);
    
    while (true) {
      try {
        SocketChannel clientChannel = serverChannel.accept();
          
        String response = "Hello " 
         + clientChannel.socket().getInetAddress() + " on port " 
         + clientChannel.socket().getPort() + "\r\n";
        response += "This is " + serverChannel.socket() + " on port " 
         + serverChannel.socket().getLocalPort() + "\r\n";
        
        byte[] data = response.getBytes("UTF-8");
        ByteBuffer buffer = ByteBuffer.wrap(data);
        while (buffer.hasRemaining()) clientChannel.write(buffer);
        clientChannel.close();
      }
      catch (IOException ex) {
        // This is an error on one connection. Maybe the client crashed.
        // Maybe it broke the connection prematurely. Whatever happened, 
        // it's not worth shutting down the server for. 
      }
    }  // end while
  } // end main
} // end NewIOHelloServer
