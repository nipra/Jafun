//Filename: ListPool.java
package smc;

public class ListPool {                          // (1) Top-level class

  public static class MyLinkedList {             // (2) Static member class

    private interface ILink { }                  // (3) Static member interface

    public static class BiNode
                  implements IBiLink { }         // (4) Static member class
  }

  interface IBiLink
            extends MyLinkedList.ILink { }       // (5) Static member interface
}