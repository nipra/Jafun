class Counter extends Thread {

  private int currentValue;

  public Counter(String threadName) {
    super(threadName);                           // (1) Initialize thread.
    currentValue = 0;
    System.out.println(this);
//  setDaemon(true);
    start();                                     // (2) Start this thread.
  }

  public int getValue() { return currentValue; }

  public void run() {                            // (3) Override from superclass.
    try {
      while (currentValue < 5) {
        System.out.println(getName() + ": " + (currentValue++));
        Thread.sleep(250);                       // (4) Current thread sleeps.
      }
    } catch (InterruptedException e) {
      System.out.println(getName() + " interrupted.");
    }
    System.out.println("Exit from thread: " + getName());
  }
}
//_______________________________________________________________________________
public class Client {
  public static void main(String[] args) {

    System.out.println("Method main() runs in thread " +
        Thread.currentThread().getName());       // (5) Current thread

    Counter counterA = new Counter("Counter A"); // (6) Create a thread.
    Counter counterB = new Counter("Counter B"); // (7) Create a thread.

    System.out.println("Exit from main() method.");
  }
}