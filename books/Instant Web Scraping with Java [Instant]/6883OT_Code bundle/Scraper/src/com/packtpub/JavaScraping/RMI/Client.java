package com.packtpub.JavaScraping.RMI;

import java.rmi.*;
import java.net.*;

public class Client {
  public static void main(String[] args) {
    try {
      String host = (args.length > 0) ? args[0] : "localhost";
      ScraperInterface remObject = (ScraperInterface)Naming.lookup("//127.0.0.1:1099/Scraper");
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