import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.*;

public class UDPTimeClient {
  
  public static void main(String[] args) throws IOException {

    DatagramChannel channel = null;
    try {
      channel = DatagramChannel.open();
      // port 0 selects any available port
      SocketAddress address = new InetSocketAddress(0);
      DatagramSocket socket = channel.socket();
      socket.setSoTimeout(5000);
      socket.bind(address);
      
      SocketAddress server = new InetSocketAddress("time.nist.gov", 37);
      ByteBuffer buffer = ByteBuffer.allocate(8192);
      // time protocol always uses big-endian order
      buffer.order(ByteOrder.BIG_ENDIAN);
      // Must put at least one byte of data in the buffer;
      // it doesn't matter what it is.
      buffer.put((byte) 65);
      buffer.flip();
      
      channel.send(buffer, server);
      
      buffer.clear();
      buffer.put((byte) 0).put((byte) 0).put((byte) 0).put((byte) 0);
      channel.receive(buffer);
      buffer.flip();
      long secondsSince1900 = buffer.getLong();
      // The time protocol sets the epoch at 1900,
      // the java.util.Date class at 1970. This number 
      // converts between them.
      long differenceBetweenEpochs = 2208988800L;

      long secondsSince1970 
       = secondsSince1900 - differenceBetweenEpochs;       
      long msSince1970 = secondsSince1970 * 1000;
      Date time = new Date(msSince1970);
      
      System.out.println(time);
    }
    catch (Exception ex) {
      System.err.println(ex); 
      ex.printStackTrace();
    }
    finally {
      if (channel != null) channel.close();
    }
  } 
}
