import java.io.IOException;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;

public class UDPTimeServer {
  
  public final static int DEFAULT_PORT = 37;
  
  public static void main(String[] args) throws IOException {
    
    int port = 37;

    if (args.length > 0) {
      try {
        port = Integer.parseInt(args[1]);
        if (port <= 0 || port > 65535) port = DEFAULT_PORT;;
      }
      catch (Exception ex){
      }
    }

    ByteBuffer in = ByteBuffer.allocate(8192);
    ByteBuffer out = ByteBuffer.allocate(8);
    out.order(ByteOrder.BIG_ENDIAN);
    SocketAddress address = new InetSocketAddress(port);
    DatagramChannel channel = DatagramChannel.open();
    DatagramSocket socket = channel.socket();
    socket.bind(address);
    System.err.println("bound to " + address);
    while (true) {
      try {
        in.clear();
        SocketAddress client = channel.receive(in);
        System.err.println(client);
        long secondsSince1900 = getTime();
        out.clear();
        out.putLong(secondsSince1900);
        out.flip();
        // skip over the first four bytes to make this an unsigned int
        out.position(4);
        channel.send(out, client);
      }
      catch (Exception ex) {
        System.err.println(ex);
      }
    }
  }
  
  private static long getTime() {
      long differenceBetweenEpochs = 2208988800L;
      Date now = new Date();
      long secondsSince1970 = now.getTime() / 1000;
      long secondsSince1900 = secondsSince1970 + differenceBetweenEpochs;
      return secondsSince1900;
  } 
}
