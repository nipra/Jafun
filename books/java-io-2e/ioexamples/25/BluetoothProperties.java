import javax.bluetooth.*;

public class BluetoothProperties {
  
  public static void main(String[] args) throws Exception {
    LocalDevice device  = LocalDevice.getLocalDevice();
    System.out.println("API version: " 
        + device.getProperty("bluetooth.api.version"));
    System.out.println("bluetooth.master.switch: " 
        + device.getProperty("bluetooth.master.switch"));
    System.out.println("Maximum number of service attributes: "
        + device.getProperty("bluetooth.sd.attr.retrievable.max"));
    System.out.println("Maximum number of connected devices: " 
        + device.getProperty("bluetooth.connected.devices.max"));
    System.out.println("Maximum receive MTU size in L2CAP: " 
        + device.getProperty("bluetooth.l2cap.receiveMTU.max"));
    System.out.println(
        "Maximum number of simultaneous service discovery transactions: " 
        + device.getProperty("bluetooth.sd.trans.max"));
    System.out.println("Inquiry scanning allowed during connection: " 
        + device.getProperty("bluetooth.connected.inquiry.scan"));
    System.out.println("Page scanning allowed during connection: " 
        + device.getProperty("bluetooth.connected.page.scan"));
    System.out.println("Inquiry allowed during connection: " 
        + device.getProperty("bluetooth.connected.inquiry"));
    System.out.println("Page allowed during connection: " 
        + device.getProperty("bluetooth.connected.page"));
    System.exit(0);
  }
}
