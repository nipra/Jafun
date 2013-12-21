import java.util.*;
import javax.usb.*;

public class USBLister {

  public static void main(String[] args) throws UsbException {
    UsbServices services = UsbHostManager.getUsbServices();
    UsbHub root = services.getRootUsbHub();
    listDevices(root);
  }
  
  public static void listDevices(UsbHub hub) {
    List devices = hub.getAttachedUsbDevices();
    Iterator iterator = devices.iterator();
    while (iterator.hasNext()) {
      UsbDevice device = (UsbDevice) iterator.next();
      System.out.println(device);
      if (device.isUsbHub()) {
        listDevices((UsbHub) device);
      }
    } 
  }
}
