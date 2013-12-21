import java.io.*;

public class FibonacciDriver {

  public static void main (String[] args) throws IOException {
    PipedOutputStream pout = new PipedOutputStream();
    PipedInputStream pin = new PipedInputStream(pout);

    FibonacciProducer fw = new FibonacciProducer(pout, 20);
    FibonacciConsumer fr = new FibonacciConsumer(pin);
    fw.start();
    fr.start();
  }
}
