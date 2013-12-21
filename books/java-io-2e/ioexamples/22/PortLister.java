import javax.comm.*;
import java.util.*;

public class PortLister {

  public static void main(String[] args) {
    Enumeration e = CommPortIdentifier.getPortIdentifiers();
    while (e.hasMoreElements()) {
      System.out.println((CommPortIdentifier) e.nextElement());
    }
  }
}
