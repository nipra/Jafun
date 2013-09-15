public class ListPool<T> {        // (1) Top level class

  static class MyLinkedList<T> {  // (2) Hiding type parameter in enclosing context
    T t;                          // T refers to (2)
  }

  class Node<E> {                 // (4) Non-static member (inner) class
    T t;                          // T refers to (1)
    E e;
  }

  public static void main(String[] args) {
    // (5) Instantiating a generic top-level class:
    ListPool<String> lp = new ListPool<String>();

    // (6) Instantiating a generic static member class:
    ListPool.MyLinkedList<String> list = new ListPool.MyLinkedList<String>();

    // (7) Instantiating a generic non-static member class:
    ListPool<String>.Node<Integer> node1 = lp.new Node<Integer>();
    ListPool<String>.Node<Double> node2 = lp.new Node<Double>();
    ListPool<Integer>.Node<String> node3
                      = new ListPool<Integer>().new Node<String>();
  }
}