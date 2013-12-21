import javax.comm.*;
import java.util.*;

public class PortTester {

  public static void main(String[] args) {

    Enumeration thePorts = CommPortIdentifier.getPortIdentifiers();
    while (thePorts.hasMoreElements()) {
      CommPortIdentifier com = (CommPortIdentifier) thePorts.nextElement();
      System.out.print(com.getName());

      switch(com.getPortType()) {
        case CommPortIdentifier.PORT_SERIAL:
          System.out.println(", a serial port: ");
          break;
        case CommPortIdentifier.PORT_PARALLEL:
          System.out.println(", a parallel port: ");
          break;
        default:
          // important since other types of ports like USB
          // and firewire are expected to be added in the future
          System.out.println(" , a port of unknown type: ");
          break;
      }

      try {
        CommPort thePort = com.open("Port Tester", 20);
        testProperties(thePort);
        thePort.close();
      }
      catch (PortInUseException ex) {
        System.out.println("Port in use, can't test properties");        
      }
      System.out.println();
    }
  }

  public static void testProperties(CommPort thePort) {

    try {
      thePort.enableReceiveThreshold(10);
      System.out.println("Receive threshold supported");      
    }
    catch (UnsupportedCommOperationException ex) {
      System.out.println("Receive threshold not supported");   
    }

    try {
      thePort.enableReceiveTimeout(10);
      System.out.println("Receive timeout not supported");      
    }
    catch (UnsupportedCommOperationException e) {
      System.out.println("Receive timeout not supported");   
    }
    
    try {
      thePort.enableReceiveFraming(10);
      System.out.println("Receive framing supported");      
    }
    catch (UnsupportedCommOperationException e) {
      System.out.println("Receive framing not supported");   
    }
  } 
}
