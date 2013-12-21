import java.io.IOException;
import javax.bluetooth.*;
import javax.microedition.io.*;

public class BluetoothReceiver {
  
  public final static String UUID = "7140b25b7bd741d6a3ad0426002febcd";

  public static void main(String[] args) {
    try {
      LocalDevice device = LocalDevice.getLocalDevice();
      // make sure other devices can find us
      device.setDiscoverable(DiscoveryAgent.GIAC);
      String url = "btl2cap://localhost:" + UUID + ";name=L2CAPExampleServer";
      
      L2CAPConnectionNotifier notifier 
        = (L2CAPConnectionNotifier) Connector.open(url);
      L2CAPConnection client = notifier.acceptAndOpen();
      byte[] buffer = new byte[client.getTransmitMTU()];
      while (true) {
        int received = client.receive(buffer);
        if (received == 1 && buffer[0] == 0) {
          System.out.println("Exiting");
          break;
        }
        System.out.write(buffer, 0, received);
      }
      
    } 
    catch (BluetoothStateException ex) {
      System.err.println("Could not initialize Bluetooth."
       + " Please make sure Bluetooth is turned on.");
    } 
    catch (IOException ex) {
      System.err.println("Could not start server");
    }
    System.exit(0);
  }
}
