package testRMI;

import java.rmi.*; // For Naming, RemoteException, etc.
import java.net.*; // For MalformedURLException
import java.io.*;  // For Serializable interface

/** Get a Rem object from the specified remote host.
 *  Use its methods as though it were a local object.
 * @see Rem
 */

public class RemClient {
  public static void main(String[] args) {
    try {
      String host =
        (args.length > 0) ? args[0] : "localhost";
      // Get remote object and store it in remObject:
      Rem remObject =
        (Rem)Naming.lookup("rmi://" + host + "/Rem");
      // Call methods in remObject:
      System.out.println(remObject.getMessage());
    } catch(RemoteException re) {
      System.out.println("RemoteException: " + re);
    } catch(NotBoundException nbe) {
      System.out.println("NotBoundException: " + nbe);
    } catch(MalformedURLException mfe) {
      System.out.println("MalformedURLException: "
                         + mfe);
    }
  }
}
