import javax.comm.*;

public class NamedPortLister {

  public static void main(String[] args) {

    // List serial (COM) ports.
    try {
      int portNumber = 1;
      while (true) {
        CommPortIdentifier.getPortIdentifier("COM" + portNumber);
        System.out.println("COM" + portNumber);
        portNumber++;
      }
    }
    catch (NoSuchPortException ex) {
      // Break out of loop.
    }

    // List parallel (LPT) ports.
    try {
      int portNumber = 1;
      while (true) {
        CommPortIdentifier.getPortIdentifier("LPT" + portNumber);
        System.out.println("LPT" + portNumber);
        portNumber++;
      }
    }
    catch (NoSuchPortException ex) {
      // Break out of loop.
    }
  }
}
