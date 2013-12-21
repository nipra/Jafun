import java.io.UnsupportedEncodingException;
import java.util.*;
import javax.usb.*;

public class USBStringLister {

  public static void main(String[] args) 
   throws UsbException, UnsupportedEncodingException {
    UsbServices services = UsbHostManager.getUsbServices();
    UsbHub root = services.getRootUsbHub();
    listDevices(root);
  }
  
  public static void listDevices(UsbHub hub) 
   throws UnsupportedEncodingException, UsbException {
    List devices = hub.getAttachedUsbDevices();
    Iterator iterator = devices.iterator();
    while (iterator.hasNext()) {
      UsbDevice device = (UsbDevice) iterator.next();
      listStrings(device);
      if (device.isUsbHub()) {
        listDevices((UsbHub) device);
      }
    } 
  }

  public static void listStrings(UsbDevice device) 
   throws UnsupportedEncodingException, UsbException {

    for (int i = 0; i <= 255; i++) {
      try {
        String s = device.getString((byte) i);
        System.out.println("  " + i + ":\t" + s);
      }
      catch (UsbStallException ex) {
        // We've reached the end of the table for this device.
        break;
      }
    }
    System.out.println();
  }
}
