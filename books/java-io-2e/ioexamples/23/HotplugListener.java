import javax.usb.*;
import javax.usb.event.*;

public class HotplugListener implements UsbServicesListener {

  public void usbDeviceAttached(UsbServicesEvent event) {
    UsbDevice device = event.getUsbDevice();
    System.out.println(getDeviceInfo(device) + " was added to the bus.");
  }

  public void usbDeviceDetached(UsbServicesEvent event) {
    UsbDevice device = event.getUsbDevice();
    System.out.println(getDeviceInfo(device) + " was removed from the bus.");
  }

  private static String getDeviceInfo(UsbDevice device) {
    try {
      String product = device.getProductString();
      String serial  = device.getSerialNumberString();
      if (product == null) return "Unknown USB device";
      if (serial != null) return product + " " + serial;
      else return product;
    }
    catch (Exception ex) {
    }
    return "Unknown USB device";
  }
}
