class Counter implements Runnable {
  private int currentValue;
  public Counter() { currentValue = 0; }
  public int getValue() { return currentValue; }

  public void run() {                            // (1) Thread entry point
    try {
      while (currentValue < 5) {
        System.out.println(
            Thread.currentThread().getName()     // (2) Print thread name.
            + ": " + (currentValue++)
        );
        Thread.sleep(250);                       // (3) Current thread sleeps.
      }
    } catch (InterruptedException e) {
      System.out.println(Thread.currentThread().getName() + " interrupted.");
    }
    System.out.println("Exit from thread: " + Thread.currentThread().getName());
  }
}
//_______________________________________________________________________________
public class Client {
  public static void main(String[] args) {
    Counter counterA = new Counter();                 // (4) Create a counter.
    Thread worker = new Thread(counterA, "Counter A");// (5) Create a new thread.
    System.out.println(worker);
    worker.start();                                   // (6) Start the thread.

    try {
      int val;
      do {
        val = counterA.getValue();               // (7) Access the counter value.
        System.out.println(
            "Counter value read by " +
            Thread.currentThread().getName() +   // (8) Print thread name.
            ": " + val
        );
        Thread.sleep(1000);                      // (9) Current thread sleeps.
      } while (val < 5);
    } catch (InterruptedException e) {
      System.out.println("The main thread is interrupted.");
    }

    System.out.println("Exit from main() method.");
  }
}