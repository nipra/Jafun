//Filename: Client2.java
import static smc.ListPool.MyLinkedList.BiNode;           // (3) Static import

public class Client2 {
  BiNode objRef2 = new BiNode();                          // (4)
}

class BiListPool implements smc.ListPool.IBiLink { }      // (5) Not accessible!