package com.packtpub.JavaScraping.RMI;

import java.rmi.*;

/** The RMI client will use this interface directly.
 *  The RMI server will make a real remote object that
 *  implements this, then register an instance of it 
 *  with some URL. 
 */

public interface ScraperInterface extends  Remote {
  public String getMessage() throws RemoteException;
}