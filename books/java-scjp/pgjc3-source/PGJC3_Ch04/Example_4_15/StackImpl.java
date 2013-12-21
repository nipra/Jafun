class StackImpl {                // Non-generic partial implementation
  private Object[] stackArray;
  private int topOfStack;
  // ...
  synchronized public void push(Object elem) { // (1)
    stackArray[++topOfStack] = elem;
  }

  synchronized public Object pop() {           // (2)
    Object obj = stackArray[topOfStack];
    stackArray[topOfStack] = null;
    topOfStack--;
    return obj;
  }

  // Other methods, etc.
  public Object peek() { return stackArray[topOfStack]; }
}