//A client for the generic class Node<T>.
public class Preliminaries {
  public static void main(String[] args) {
    Node<Integer> intNode = new Node<Integer>(2008, null);
    Integer iRef = intNode.getData();               // 2008
    intNode.setData(2010);                          // Ok.
//  intNode.setData("TwentyTen");                   // (1) Compile-time error!
    intNode.setNext(new Node<Integer>(2009, null)); // (2010, (2009, null))
//  intNode.setNext(new Node<String>("Hi", null));  // (2) Compile-time error!

    Node<String> strNode = new Node<String>("hi", null);
//  intNode = strNode;              // (3) Compile-time error!
    String str = strNode.getData(); // (4) No explicit cast necessary.

    Node rawNode = intNode;         // (5) Assigning to raw type always possible.
    rawNode.setData("BOOM");        // (6) Unchecked call warning!
    intNode = rawNode;              // (7) Unchecked conversion warning!
    iRef = intNode.getData();       // (8) ClassCastException!
  }
}