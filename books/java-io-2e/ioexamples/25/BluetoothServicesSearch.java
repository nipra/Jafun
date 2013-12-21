import java.io.IOException;
import javax.bluetooth.*;

public class BluetoothServicesSearch implements DiscoveryListener {
  
  private DiscoveryAgent agent;
  private final static UUID L2CAP = new UUID(0x0100);
  
  public static void main(String[] args) throws Exception { 
    BluetoothServicesSearch search = new BluetoothServicesSearch();
    search.agent = LocalDevice.getLocalDevice().getDiscoveryAgent();
    search.agent.startInquiry(DiscoveryAgent.GIAC, search);
  }

  public void deviceDiscovered(RemoteDevice device, DeviceClass type) {
    try {
      System.out.println("Found " + device.getFriendlyName(false)
          + " at " + device.getBluetoothAddress());
    } 
    catch (IOException ex) {
      System.out.println("Found unnamed device " 
          + " at " + device.getBluetoothAddress());
    }
    searchServices(device);
  }
  
  public final static int SERVICE_RECORD_HANDLE             = 0X0000;
  public final static int SERVICE_CLASSID_LIST              = 0X0001;
  public final static int SERVICE_RECORD_STATE              = 0X0002;
  public final static int SERVICE_ID                        = 0X0003;
  public final static int PROTOCOL_DESCRIPTOR_LIST          = 0X0004;
  public final static int BROWSE_GROUP_LIST                 = 0X0005;
  public final static int LANGUAGE_BASED_ATTRIBUTE_ID_LIST  = 0X0006;
  public final static int SERVICE_INFO_TIME_TO_LIVE         = 0X0007;
  public final static int SERVICE_AVAILABILITY              = 0X0008;
  public final static int BLUETOOTH_PROFILE_DESCRIPTOR_LIST = 0X0009;
  public final static int DOCUMENTATION_URL                 = 0X000A;
  public final static int CLIENT_EXECUTABLE_URL             = 0X000B;
  public final static int ICON_URL                          = 0X000C;
  public final static int VERSION_NUMBER_LIST               = 0X0200;
  public final static int SERVICE_DATABASE_STATE            = 0X0201;
  
  private void searchServices(RemoteDevice device) {
    
    UUID[] searchList = {L2CAP};
    int[] attributes = {SERVICE_RECORD_HANDLE, SERVICE_CLASSID_LIST, 
                        SERVICE_RECORD_STATE, SERVICE_ID, 
                        PROTOCOL_DESCRIPTOR_LIST, BROWSE_GROUP_LIST, 
                        LANGUAGE_BASED_ATTRIBUTE_ID_LIST,
                        SERVICE_INFO_TIME_TO_LIVE, SERVICE_AVAILABILITY, 
                        BLUETOOTH_PROFILE_DESCRIPTOR_LIST, DOCUMENTATION_URL, 
                        CLIENT_EXECUTABLE_URL, ICON_URL, VERSION_NUMBER_LIST, 
                        SERVICE_DATABASE_STATE};
    try {
    System.out.println("Searching " + device.getBluetoothAddress() 
       + " for services");
      int trans = this.agent.searchServices(attributes, searchList, device, this);
      System.out.println("Service Search " + trans + " started");
    }
    catch (BluetoothStateException ex) {
      System.out.println( "BluetoothStateException: " + ex.getMessage() );
    }    
    
  }
  
  public void servicesDiscovered(int transactionID, ServiceRecord[] record) {
      for (int i = 0; i < record.length; i++) {
        System.out.println("Found service " + record[i].getConnectionURL(
         ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false)); 
      }   
  }

  public void serviceSearchCompleted(int transactionID, int responseCode) {
    
    switch (responseCode) {
      case DiscoveryListener.SERVICE_SEARCH_DEVICE_NOT_REACHABLE:
        System.out.println("Could not find device on search " + transactionID);
        break;
      case DiscoveryListener.SERVICE_SEARCH_ERROR:
        System.out.println("Error searching device on search " + transactionID);
        break;
      case DiscoveryListener.SERVICE_SEARCH_NO_RECORDS:
        System.out.println("No service records on device on search " 
         + transactionID);
        break;
      case DiscoveryListener.SERVICE_SEARCH_TERMINATED:
        System.out.println("User cancelled search " + transactionID);
        break;
      case DiscoveryListener.SERVICE_SEARCH_COMPLETED:
        System.out.println("Service search " + transactionID + " complete");
        break;
      default:
        System.out.println("Unexpected response code " + responseCode 
         + " from search " + transactionID);        
    }
  }

  public void inquiryCompleted(int transactionID) {
    System.out.println("Device search " + transactionID + " complete");
  }
}
