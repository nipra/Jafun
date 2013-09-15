import java.util.Iterator;

/** Iterator for nodes */
public class NodeIterator<E> implements Iterator<E> {
  private Node<E> thisNode;

  public NodeIterator(Node<E> first) { thisNode = first;  }

  public boolean hasNext() { return thisNode != null; }

  public E next() {
    E data = thisNode.getData();
    thisNode = thisNode.getNext();
    return data;
  }

  public void remove() { throw new UnsupportedOperationException(); }
}