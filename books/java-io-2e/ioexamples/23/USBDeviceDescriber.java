import java.io.UnsupportedEncodingException;
import java.util.*;
import javax.usb.*;

public class USBDeviceDescriber {

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
      describe(device);
      if (device.isUsbHub()) {
        listDevices((UsbHub) device);
      }
    } 
  }

  public static void describe(UsbDevice device) 
   throws UnsupportedEncodingException, UsbException {
        UsbDeviceDescriptor descriptor = device.getUsbDeviceDescriptor();
    byte manufacturerCode = descriptor.iManufacturer();
    System.out.println("Manufacturer index: " + manufacturerCode); 
    System.out.println("Manufacturer string: " 
     + device.getString(manufacturerCode)); 
    byte productCode = descriptor.iProduct();
    System.out.println("Product index: " + productCode); 
    System.out.println("Product string: " + device.getString(productCode)); 
    byte serialCode = descriptor.iSerialNumber();
    System.out.println("Serial number index: " + serialCode); 
    System.out.println("Serial Number string: " + device.getString(serialCode)); 

    System.out.println("Vendor ID: 0x" 
     + Integer.toHexString(descriptor.idVendor())); 
    System.out.println("Product ID: 0x" 
     + Integer.toHexString(descriptor.idProduct())); 
    System.out.println("Class: " + descriptor.bDeviceClass()); 
    System.out.println("Subclass: " + descriptor.bDeviceSubClass()); 
    System.out.println("Protocol: " + descriptor.bDeviceProtocol()); 
    System.out.println("Device version: " + decodeBCD(descriptor.bcdDevice())); 
    System.out.println("USB version: " + decodeBCD(descriptor.bcdUSB())); 
    System.out.println("Maximum control packet size: " 
     + descriptor.bMaxPacketSize0()); 
    System.out.println("Number of configurations: " 
     + descriptor.bNumConfigurations()); 

    System.out.println();
  }

  public static String decodeBCD(short bcd) {
    int upper = (0xFF00 & bcd) >> 8;
    int middle = (0xF0 & bcd) >> 4;
    int lower = 0x0F & bcd;
    return upper + "." + middle + "." + lower;
  } 
}
