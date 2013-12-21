import java.io.*;
import javax.bluetooth.*;
import javax.microedition.io.*;

public class BluetoothTransmitter {
  
  public static void main(String[] args) {
    
    try {
      String url = BluetoothServiceFinder.getConnectionURL(BluetoothReceiver.UUID);
      if (url == null) {
        System.out.println("No receiver in range");
        return; 
      }
      System.out.println("Connecting to " + url);
      L2CAPConnection conn = (L2CAPConnection) Connector.open(url);
      int mtu = conn.getTransmitMTU(); // maximum packet length we can send
      // use safe???
      BufferedReader reader = new BufferedReader(
       new InputStreamReader(System.in));
      
      while (true) {
        String line = reader.readLine();
        if (".".equals(line)) {
          byte[] end = {0};
          conn.send(end);
          break; 
        }
        line += "\r\n";
        // Now we need to make sure this fits into the MTU
        byte[][] packets = segment(line, mtu);
        for (int i = 0; i < packets.length; i++) {
          conn.send(packets[i]);
        }
      }
      
    } 
    catch (IOException ex) {
      ex.printStackTrace();
    }
    System.exit(0);
  }

  private static byte[][] segment(String line, int mtu) {
    
    int numPackets = (line.length()-1)/mtu + 1;
    byte[][] packets = new byte[numPackets][mtu];
    try {
      byte[] data = line.getBytes("UTF-8");
      // The last packet will normally not fill a complete MTU
      for (int i = 0; i < numPackets-1; i++) {
        System.arraycopy(data, i*mtu, packets[i], 0, mtu );
      }
      System.arraycopy(data, (numPackets-1)*mtu, packets[numPackets-1],
                       0, data.length - ((numPackets-1)*mtu) );
      return packets;
    } 
    catch (UnsupportedEncodingException ex) {
      throw new RuntimeException("Broken VM does not support UTF-8");
    }
  }
}
