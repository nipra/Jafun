package com.packtpub.JavaScraping.RMI;

import java.rmi.*;
import java.net.*;

/** The server creates a RemImpl (which implements
 *  the Rem interface), then registers it with
 *  the URL Rem, where clients can access it.
 */

public class Server {
  public static void main(String[] args) {
    try {
      ScraperImp localObject = new ScraperImp();
      Naming.rebind("rmi://localhost:1099/Scraper", localObject);
      System.out.println("Successfully bound Remote Implementation!");
    } catch(RemoteException re) {
      System.out.println("RemoteException: " + re);
    } catch(MalformedURLException mfe) {
      System.out.println("MalformedURLException: "
                         + mfe);
    }
  }
}