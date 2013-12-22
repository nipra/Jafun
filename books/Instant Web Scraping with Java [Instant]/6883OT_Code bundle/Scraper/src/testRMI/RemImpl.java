package testRMI;

import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;

/** This is the actual implementation of Rem that
 *  the RMI server uses. The server builds an instance
 *  of this then registers it with a URL. The
 *  client accesses the URL and binds the result to
 *  a Rem (not a RemImpl; it doesn't have this).
 */

public class RemImpl extends UnicastRemoteObject implements Rem {
  public RemImpl() throws RemoteException {}

  public String getMessage() throws RemoteException {
    return("Here is a remote message.");
  }
}