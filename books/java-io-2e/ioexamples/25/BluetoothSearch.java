import java.io.IOException;
import javax.bluetooth.*;

public class BluetoothSearch implements DiscoveryListener {
  
  private DiscoveryAgent agent;
  
  public static void main(String[] args) throws Exception { 
    BluetoothSearch search = new BluetoothSearch();
    search.agent = LocalDevice.getLocalDevice().getDiscoveryAgent();
    search.agent.startInquiry(DiscoveryAgent.GIAC, search);
  }

  public void deviceDiscovered(RemoteDevice device, DeviceClass type) {
    int major = type.getMajorDeviceClass();
    int minor = type.getMinorDeviceClass();
    int services = type.getServiceClasses();
    int classIdentifier = major | minor | services;
    try {
      System.out.println("Found " + device.getFriendlyName(false)
       + " at " + device.getBluetoothAddress());
    } 
    catch (IOException ex) {
      System.out.println("Found unnamed device " 
       + " at " + device.getBluetoothAddress());
    }
    System.out.println("  Major class: 0x" + Integer.toHexString(major));
    System.out.println("  Minor class: 0x" + Integer.toHexString(minor));
    System.out.println("  Service classes: 0x" + Integer.toHexString(services));
    System.out.println("  Class identifier: 0x" 
     + Integer.toHexString(classIdentifier));
    System.out.println("  Class identifier: " 
     + Integer.toBinaryString(classIdentifier));
  }
  
  public void inquiryCompleted(int discoveryType) {
    
    switch (discoveryType) {
      case DiscoveryListener.INQUIRY_TERMINATED:
        System.out.println("Search cancelled");
        break;
      case DiscoveryListener.INQUIRY_ERROR:
        System.out.println("Bluetooth error");
        break;
      case DiscoveryListener.INQUIRY_COMPLETED:
        System.out.println("Device search complete");;
        break;
      default: 
        System.out.println("Unanticipated result: " + discoveryType);
    }
    
    System.exit(0);
  }
  
  // This search is only looking for devices and won’t discover any services;
  // but we have to implement these methods to fulfill the interface
  public void servicesDiscovered(int transactionID, ServiceRecord[] record) {}
  public void serviceSearchCompleted(int transactionID, int arg1) {}
}
