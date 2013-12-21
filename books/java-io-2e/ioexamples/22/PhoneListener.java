import javax.comm.*;
import java.util.TooManyListenersException;

public class PhoneListener implements SerialPortEventListener {

  public static void main(String[] args) {
  
    String portName = "COM1";
    if (args.length > 0) portName = args[0];
    
    PhoneListener pl = new PhoneListener();
    
    try {
      CommPortIdentifier cpi = CommPortIdentifier.getPortIdentifier(portName);
      if (cpi.getPortType() == CommPortIdentifier.PORT_SERIAL) {
        SerialPort modem = (SerialPort) cpi.open("Phone Listener", 1000);
        modem.notifyOnRingIndicator(true);
        modem.addEventListener(pl);
      }
    }
    catch (NoSuchPortException ex) {
      System.err.println("Usage: java PhoneListener port_name");
    }
    catch (TooManyListenersException ex) {
      // shouldn't happen in this example
    }
    catch (PortInUseException ex) {System.err.println(ex);}
  }

  public void serialEvent(SerialPortEvent evt) {
  
    System.err.println(evt.getEventType());
    if (evt.getEventType() == SerialPortEvent.RI) {
      System.out.println("The phone is ringing");
    }
  }
} 
