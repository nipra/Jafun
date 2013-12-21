import javax.comm.*;
import java.util.*;

public class PrettyPortLister {

  public static void main(String[] args) {

    Enumeration e = CommPortIdentifier.getPortIdentifiers();
    while (e.hasMoreElements()) {
      CommPortIdentifier com = (CommPortIdentifier) e.nextElement();
      System.out.print(com.getName());

      switch(com.getPortType()) {
        case CommPortIdentifier.PORT_SERIAL:
          System.out.print(", a serial port, ");
          break;
        case CommPortIdentifier.PORT_PARALLEL:
          System.out.print(", a parallel port, ");
          break;
        default:
          // Important since other types of ports like USB
          // and firewire are expected to be added in the future.
          System.out.print(" , a port of unknown type, ");
          break;
      }
      if (com.isCurrentlyOwned()) {
        System.out.println("is currently owned by " 
         + com.getCurrentOwner() + ".");
      }
      else {
        System.out.println("is not currently owned.");
      }
    }
  }
}
