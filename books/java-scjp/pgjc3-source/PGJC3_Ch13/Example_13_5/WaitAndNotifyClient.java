class StackImpl {
  private Object[] stackArray;
  private volatile int topOfStack;

  StackImpl (int capacity) {
    stackArray = new Object[capacity];
    topOfStack = -1;
  }

  public synchronized Object pop() {
    System.out.println(Thread.currentThread() + ": popping");
    while (isEmpty())
      try {
        System.out.println(Thread.currentThread() + ": waiting to pop");
        wait();                                      // (1)
      } catch (InterruptedException ie) {
        System.out.println(Thread.currentThread() + " interrupted.");
      }
    Object element = stackArray[topOfStack];
    stackArray[topOfStack--] = null;
    System.out.println(Thread.currentThread() +
                       ": notifying after popping");
    notify();                                        // (2)
    return element;
  }

  public synchronized void push(Object element) {
    System.out.println(Thread.currentThread() + ": pushing");
    while (isFull())
      try {
        System.out.println(Thread.currentThread() + ": waiting to push");
        wait();                                      // (3)
      } catch (InterruptedException ie) {
        System.out.println(Thread.currentThread() + " interrupted.");
      }
    stackArray[++topOfStack] = element;
    System.out.println(Thread.currentThread() +
                       ": notifying after pushing");
    notify();                                        // (4)
  }

  public boolean isFull() { return topOfStack >= stackArray.length -1; }
  public boolean isEmpty() { return topOfStack < 0; }
}
//_______________________________________________________________________________
abstract class StackUser implements Runnable {       // (5) Stack user

  protected StackImpl stack;                         // (6)

  StackUser(String threadName, StackImpl stack) {
    this.stack = stack;
    Thread worker = new Thread(this, threadName);
    System.out.println(worker);
    worker.setDaemon(true);                          // (7) Daemon thread status
    worker.start();                                  // (8) Start the thread
  }
}
//_______________________________________________________________________________
class StackPopper extends StackUser {                // (9) Popper
  StackPopper(String threadName, StackImpl stack) {
    super(threadName, stack);
  }
  public void run() { while (true) stack.pop(); }
}
//_______________________________________________________________________________
class StackPusher extends StackUser {                // (10) Pusher
  StackPusher(String threadName, StackImpl stack) {
    super(threadName, stack);
  }
  public void run() { while (true) stack.push(2008); }
}
//_______________________________________________________________________________
public class WaitAndNotifyClient {
  public static void main(String[] args)
                     throws InterruptedException {   // (11)

    StackImpl stack = new StackImpl(5);              // Stack of capacity 5.

    new StackPusher("A", stack);
    new StackPusher("B", stack);
    new StackPopper("C", stack);
    System.out.println("Main Thread sleeping.");
    Thread.sleep(10);
    System.out.println("Exit from Main Thread.");
  }
}