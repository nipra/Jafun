//Filename: ListPool.java
public class ListPool {                                 // Top-level class

  public void messageInListPool() {                     // Instance method
    System.out.println("This is a ListPool object.");
  }

  private static class MyLinkedList {                   // (1) Static class
    private static int maxNumOfLists = 100;             // Static variable
    private int currentNumOfLists;                      // Instance variable

    public static void messageInLinkedList() {          // Static method
      System.out.println("This is MyLinkedList class.");
    }

    interface ILink { int MAX_NUM_OF_NODES = 2000; }    // (2) Static interface

    protected static class Node implements ILink {      // (3) Static class

      private int max = MAX_NUM_OF_NODES;               // (4) Instance variable

      public void messageInNode() {                     // Instance method
        //  int currentLists = currentNumOfLists;       // (5) Not OK.
        int maxLists = maxNumOfLists;
        int maxNodes = max;

        //  messageInListPool();                        // (6) Not OK.
        messageInLinkedList();                          // (7) Call static method
      }

      public static void main (String[] args) {
        int maxLists = maxNumOfLists;                   // (8)
        // int maxNodes = max;                          // (9) Not OK.
        messageInLinkedList();                          // (10) Call static method
      }
    }  // Node
  }  // MyLinkedList
} // ListPool