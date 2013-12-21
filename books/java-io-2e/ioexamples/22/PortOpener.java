import javax.comm.*;
import java.util.*;

public class PortOpener {

  public static void main(String[] args) {

    Enumeration thePorts = CommPortIdentifier.getPortIdentifiers();
    while (thePorts.hasMoreElements()) {
      CommPortIdentifier com = (CommPortIdentifier) thePorts.nextElement();
      System.out.print(com.getName());

      switch(com.getPortType()) {
        case CommPortIdentifier.PORT_SERIAL:
          System.out.print(", a serial port, ");
          break;
        case CommPortIdentifier.PORT_PARALLEL:
          System.out.print(", a parallel port, ");
          break;
        default:
          // important since other types of ports like USB
          // and firewire are expected to be added in the future
          System.out.print(" , a port of unknown type, ");
          break;
      }
      try {
        CommPort thePort  = com.open("PortOpener", 10);
        System.out.println("is not currently owned.");
        thePort.close(); 
      }
      catch (PortInUseException ex) {
        String owner = com.getCurrentOwner();
        if (owner == null) owner = "unknown";
        System.out.println("is currently owned by " + owner + ".");
      }
    }
  }
}
