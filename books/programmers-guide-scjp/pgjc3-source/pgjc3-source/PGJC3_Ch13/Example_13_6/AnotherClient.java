//_______________________________________________________________________________
public class AnotherClient {
  public static void main(String[] args) {

    Counter counterA = new Counter("Counter A");
    Counter counterB = new Counter("Counter B");

    try {
      System.out.println("Wait for the child threads to finish.");
      counterA.join();                                 // (5)
      if (!counterA.isAlive())                         // (6)
        System.out.println("Counter A not alive.");
      counterB.join();                                 // (7)
      if (!counterB.isAlive())                         // (8)
        System.out.println("Counter B not alive.");
    } catch (InterruptedException ie) {
      System.out.println("Main Thread interrupted.");
    }
    System.out.println("Exit from Main Thread.");
  }
}