//Filename: Counter.java
/*
    Notice that the result of running this program
    may not be what you expect. Since both threads are
    working full throttle it is possible that only one
    of the threads is granted CPU time.
 */

public class Counter implements Runnable {
  public static void main(String[] args) {
    Storage store = new Storage();
    new Counter(store);
    new Printer(store);
  }
  Storage storage;
  Counter(Storage target) {
    storage = target;
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

class Printer implements Runnable {
  Storage storage;
  Printer(Storage source) {
    storage = source;
    new Thread(this).start();
  }
  public void run() {
    while (true) {
      System.out.println(storage.getValue());
    }
  }
}

class Storage {
  int value;
  void setValue(int i) { value = i; }
  int getValue() { return value; }
}