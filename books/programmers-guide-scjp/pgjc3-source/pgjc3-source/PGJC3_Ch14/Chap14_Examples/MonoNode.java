class MonoNode<E> implements IMonoLink<E> {
  private E                data;    // Data
  private IMonoLink<E>     next;    // Reference to next node     // (1)
  MonoNode(E data, IMonoLink<E> next) {                           // (2)
    this.data = data;
    this.next = next;
  }
  public void    setData(E data)       { this.data = data; }
  public E       getData()             { return this.data; }
  public void    setNext(IMonoLink<E> next) { this.next = next; } // (2)
  public IMonoLink<E> getNext()             { return this.next; } // (3)
  public String toString() {
    return this.data.toString() +
          (this.next == null? "" : ", " + this.next.toString());
  }
}