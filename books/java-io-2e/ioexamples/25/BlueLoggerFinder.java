import java.io.IOException;
import javax.bluetooth.*;

public class BlueLoggerFinder implements DiscoveryListener {
  
  private DiscoveryAgent agent;
  private RemoteDevice device;
  
  public static RemoteDevice find() 
   throws BluetoothStateException { 
    BlueLoggerFinder search = new BlueLoggerFinder();
    search.agent = LocalDevice.getLocalDevice().getDiscoveryAgent();
    search.agent.startInquiry(DiscoveryAgent.GIAC, search);
    // wait for inquiry to finish
    synchronized(search){
      try {
        search.wait();
      } 
      catch (InterruptedException ex) {
        // continue
      }
    }
    return search.device;
  }

  public void deviceDiscovered(RemoteDevice device, DeviceClass type) {
    int major = type.getMajorDeviceClass();
    try {
      if (device.getFriendlyName(false).startsWith("Earthmate Blue Logger")) {
        this.device = device;
        // stop looking for other devices
        agent.cancelInquiry(this);
        // wake up the main thread
        synchronized(this){
          this.notify();
        }
      }
    } 
    catch (IOException ex) {
      // Hopefully this isn't the device we're looking for
    }
  }
  
  public void inquiryCompleted(int discoveryType) {}
  
  // This search is only looking for devices and won’t discover any services;
  // but we have to implement these methods to fulfill the interface
  public void servicesDiscovered(int transactionID, ServiceRecord[] record) {}
  public void serviceSearchCompleted(int transactionID, int arg1) {}
}
