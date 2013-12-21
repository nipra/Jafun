import javax.bluetooth.*;

public class BluetoothTest {
  
  public static void main(String[] args) throws Exception {
    LocalDevice device = LocalDevice.getLocalDevice();
    System.out.print(device.getFriendlyName() + " at ");
    System.out.print(device.getBluetoothAddress());
    System.exit(0);
  }
}
