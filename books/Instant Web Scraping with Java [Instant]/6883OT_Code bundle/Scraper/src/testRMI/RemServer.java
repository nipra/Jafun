package testRMI;

import java.rmi.*;
import java.net.*;

/** The server creates a RemImpl (which implements
 *  the Rem interface), then registers it with
 *  the URL Rem, where clients can access it.
 */

public class RemServer {
  public static void main(String[] args) {
    try {
      RemImpl localObject = new RemImpl();
      Naming.rebind("rmi:///Rem", localObject);
    } catch(RemoteException re) {
      System.out.println("RemoteException: " + re);
    } catch(MalformedURLException mfe) {
      System.out.println("MalformedURLException: "
                         + mfe);
    }
  }
}