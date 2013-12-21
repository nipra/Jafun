package collections;

public class SynchronizedArrayStack implements Stack {
  private final Stack stack;
  public SynchronizedArrayStack(Stack stack) {
    this.stack = stack;
  }
  public synchronized void push(int elt) { stack.push(elt); }
  public synchronized int pop() { return stack.pop(); }
  public synchronized boolean isEmpty() { return stack.isEmpty(); }

  public void notThreadSafe() {
    Stack stack = new SynchronizedArrayStack(new ArrayStack());

    // don't do this in a multi-threaded environment
    if (!stack.isEmpty()) {
      stack.pop();              // can throw IllegalStateExceptionindexException{IllegalStateException}
    }
    synchronized(stack) {
      if (!stack.isEmpty()) {
        stack.pop();
      }
    }
  }
}
