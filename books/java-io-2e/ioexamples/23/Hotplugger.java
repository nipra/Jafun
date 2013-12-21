import javax.usb.*;
import javax.usb.event.*;

public class Hotplugger {

  public static void main(String[] args) 
   throws UsbException, InterruptedException {
    UsbServices services = UsbHostManager.getUsbServices();
    services.addUsbServicesListener(new HotplugListener());
    // Keep this program from exiting immediately
    Thread.sleep(500000);
  }
}
