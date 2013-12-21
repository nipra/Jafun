import javax.bluetooth.*;
import java.util.Vector;

public class BluetoothServiceFinder implements DiscoveryListener {

  public static String getConnectionURL(String uuid) throws BluetoothStateException {
    BluetoothServiceFinder finder 
     = new BluetoothServiceFinder(BluetoothReceiver.UUID);
    return finder.getFirstURL();
  }  
  
  private DiscoveryAgent agent;
  private int            serviceSearchCount;
  private ServiceRecord  record;
  // I'd rather use ArrayList, but Vector is more 
  // commonly available in J2ME environments
  private Vector         devices = new Vector();
  private String         uuid;
  
  // Every search has an ID that allows it to be cancelled.
  // We need to store these so we can tell when all searches
  // are complete.
  private int[] transactions;

  private BluetoothServiceFinder(String serviceUUID) 
   throws BluetoothStateException { 
    this.uuid = serviceUUID;
    agent = LocalDevice.getLocalDevice().getDiscoveryAgent();
    int maxSimultaneousSearches = Integer.parseInt(
     LocalDevice.getProperty("bluetooth.sd.trans.max"));
    transactions = new int[maxSimultaneousSearches];
    // We need to initialize the transactions list with illegal
    // values. According to spec, the transaction ID is supposed to be
    // positive, and thus non-zero. However, several implementations
    // get this wrong and use zero as a transaction ID.
    for (int i = 0; i < maxSimultaneousSearches; i++) {
      transactions[i] = -1;
    }
  }
  
  private void addTransaction(int transactionID) {
    for (int i = 0; i < transactions.length; i++) {
      if (transactions[i] == -1) {
        transactions[i] = transactionID;
        return;
      }
    }
  }

  private void removeTransaction(int transactionID) {
    for (int i = 0; i < transactions.length; i++) {
      if (transactions[i] == transactionID) {
        transactions[i] = -1;
        return;
      }
    }
  }

  private boolean searchServices(RemoteDevice[] devices) {
    UUID[] searchList = { new UUID(uuid, false) };
    for (int i = 0; i < devices.length; i++) {
      if (record != null) {
        return true;
      }
      try {
                                         // don't care about attributes
        int transactionID = agent.searchServices(null, searchList, devices[i], this);
        addTransaction(transactionID);
      } 
      catch (BluetoothStateException ex) {
      }

      synchronized (this) {
        serviceSearchCount++;
        if (serviceSearchCount == transactions.length) {
          try {
            this.wait();
          } 
          catch (InterruptedException ex) {
            // continue
          }
        }
      }
    }

    while (serviceSearchCount > 0) { // unfinished searches
      synchronized (this) {
        try {
          this.wait();
        } 
        catch (InterruptedException ex) {
          // continue
        }
      }
    }
    if (record != null) return true;
    else return false;
  }

  private String getFirstURL() {
    try {
      agent.startInquiry(DiscoveryAgent.GIAC, this);
      synchronized (this) {
        try {
          this.wait();
        } 
        catch (InterruptedException ex) {
        }
      }
    } 
    catch (BluetoothStateException ex) {
      System.out.println("No devices in range");
    }

    if (devices.size() > 0) {
      RemoteDevice[] remotes = new RemoteDevice[devices.size()];
      devices.copyInto(remotes);
      if (searchServices(remotes)) {
        return record.getConnectionURL(
          ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false);
      }
    }
    return null;
  }

  // DiscoveryListener methods
  public void deviceDiscovered(RemoteDevice device, DeviceClass type) {
    devices.addElement(device);
  }

  public void serviceSearchCompleted(int transactionID, int responseCode) {
    removeTransaction(transactionID);
    serviceSearchCount--;
    synchronized (this) {
      this.notifyAll();
    }
  }

  public void servicesDiscovered(int transactionID, ServiceRecord[] records) {
    if (record == null) {
      record = records[0];
      for (int i = 0; i < transactions.length; i++) {
        if (transactions[i] != -1) {
          agent.cancelServiceSearch(transactions[i]);
        }
      }
    }
  }

  public void inquiryCompleted(int discType) {
    synchronized (this) {
      this.notifyAll();
    }
  }
}
