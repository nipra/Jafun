import java.io.UnsupportedEncodingException;
import java.util.*;
import javax.usb.*;

public class PrettyUSBDeviceLister {

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
      System.out.println(device.getProductString());
      System.out.println(device.getSerialNumberString());
      System.out.println(device.getManufacturerString());
      System.out.println();
      if (device.isUsbHub()) {
        listDevices((UsbHub) device);
      }
    } 
  }
}
