//Filename: Counter.java
package pe13_2;
/* Only the Storage class has been altered. */

/* No change to this class */
public class Counter implements Runnable {
  public static void main(String[] args) {
    Storage store = new Storage();
    new Counter(store);
    new Printer(store);
  }
  Storage storage;
  Counter(Storage s) {
    storage = s;
    new Thread(this).start();
  }
  public void run() {
    int i=0;
    while (true) {
      storage.setValue(i);
      i++;
    }
  }
}

/* No changes to this class. */
class Printer implements Runnable {
  Storage storage;
  Printer(Storage s) {
    storage = s;
    new Thread(this).start();
  }
  public void run() {
    while (true) {
      System.out.println(storage.getValue());
    }
  }
}

/* This class now ensures that getting and setting are done
   in an alternating fashion.
 */
class Storage {
  int value;
  boolean isUnread = false;
  synchronized void setValue(int i) {
    ensureUnread(false);
    value = i;
    setUnread(true);
  }
  synchronized int getValue() {
    ensureUnread(true);
    setUnread(false);
    return value;
  }
  private void ensureUnread(boolean shouldHaveUnread) {
    while (shouldHaveUnread != isUnread)
      try { wait(); }
      catch (InterruptedException ie) {}
  }
  private void setUnread(boolean b) {
    isUnread = b;
    notify();
  }
}