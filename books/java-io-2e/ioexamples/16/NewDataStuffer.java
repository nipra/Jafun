import java.net.*;
import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;

public class NewDataStuffer {

  private static byte[] data = new byte[255];
  
  public static void main(String[] args) throws IOException {

    for (int i = 0; i < data.length; i++) data[i] = (byte) i;
    
    ServerSocketChannel server = ServerSocketChannel.open();
    server.configureBlocking(false);
    
    server.socket().bind(new InetSocketAddress(9000));
    Selector selector = Selector.open();
    server.register(selector, SelectionKey.OP_ACCEPT);
      
    while (true) {
      selector.select();
      Set readyKeys = selector.selectedKeys();
      Iterator iterator = readyKeys.iterator();
      while (iterator.hasNext()) {
        SelectionKey key = (SelectionKey) iterator.next();
        iterator.remove();
        try {
          if (key.isAcceptable()) {
            SocketChannel client = server.accept();
            System.out.println("Accepted connection from " + client);
            client.configureBlocking(false);
            ByteBuffer source = ByteBuffer.wrap(data);
            SelectionKey key2 = client.register(selector, SelectionKey.OP_WRITE);
            key2.attach(source);
          }
          else if (key.isWritable()) {
            SocketChannel client = (SocketChannel) key.channel();
            ByteBuffer output = (ByteBuffer) key.attachment();
            if (!output.hasRemaining()) {
              output.rewind();
            }
            client.write(output);
          }
        }
        catch (IOException ex) {
          key.cancel();
          try {
            key.channel().close();
          }
          catch (IOException cex) {}
        } 
      }
    }
  }
}
