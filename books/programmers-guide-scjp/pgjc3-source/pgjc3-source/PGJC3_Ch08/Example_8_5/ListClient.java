class MyLinkedList {                                               // (1)
  private String message = "Shine the light";                      // (2)

  public Node makeInstance(String info, Node next) {               // (3)
    return new Node(info, next);                                   // (4)
  }

  public class Node {                                              // (5) NSMC
    //  static int maxNumOfNodes = 100;                            // (6) Not OK.
    final static int maxNumOfNodes = 100;                          // (7) OK.
    private String nodeInfo;                                       // (8)
    private Node next;

    public Node(String nodeInfo, Node next) {                      // (9)
      this.nodeInfo = nodeInfo;
      this.next = next;
    }

    public String toString() {
      return message + " in " + nodeInfo + " (" + maxNumOfNodes + ")"; // (10)
    }
  }
}

public class ListClient {                                           // (11)
  public static void main(String[] args) {                          // (12)
    MyLinkedList list = new MyLinkedList();                         // (13)
    MyLinkedList.Node node1 = list.makeInstance("node1", null);     // (14)
    System.out.println(node1);                                      // (15)
//  MyLinkedList.Node nodeX
//        = new MyLinkedList.Node("nodeX", node1);                  // (16) Not OK.
    MyLinkedList.Node node2 = list.new Node("node2", node1);        // (17)
    System.out.println(node2);                                      // (18)
  }
}