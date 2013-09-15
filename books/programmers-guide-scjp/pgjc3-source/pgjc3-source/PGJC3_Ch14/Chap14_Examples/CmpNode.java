class CmpNode<E extends Comparable<E>>
                extends Node<E> implements Comparable<CmpNode<E>> {

  CmpNode(E data, CmpNode<E> next) {
    super(data, next);
  }
  
  @Override
  public boolean equals(CmpNode node2) {           // (1) Compile-time error.
//public boolean equals(Object node2) {            // (1') Correct header.
    return this.compareTo(node2) == 0;
  }
  
  @Override
  public int compareTo(Object node2) {              // (2) Compile-time error.
//public int compareTo(CmpNode<E> node2) {          // (2') Correct header
    return this.getData().compareTo(node2.getData());
  }
}