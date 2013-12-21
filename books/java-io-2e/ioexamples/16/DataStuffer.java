import java.net.*;
import java.io.*;

public class DataStuffer {

  private static byte[] data = new byte[255];
  
  public static void main(String[] args) throws IOException {
    
    int port = 9000;
    for (int i = 0; i < data.length; i++) data[i] = (byte) i;
    
    ServerSocket server = new ServerSocket(port);
    while (true) {
      Socket socket = server.accept();
      Thread stuffer = new StuffThread(socket);
      stuffer.start();
    }
    
  }
  
  private static class StuffThread extends Thread {

    private Socket socket;
    
    public StuffThread(Socket socket) {
      this.socket = socket;
    }
    
    public void run() {
      try {
        OutputStream out = new BufferedOutputStream(socket.getOutputStream());
        while (!socket.isClosed()) {
          out.write(data);
        }
      }
      catch (IOException ex) {
        if (!socket.isClosed()) {
          try {
            socket.close();
          } 
          catch (IOException e) {
            // Oh well. We tried
          }
        }
      }
    }
  }
}
