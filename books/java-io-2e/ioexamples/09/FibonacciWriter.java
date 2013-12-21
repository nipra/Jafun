import java.io.*;

public class FibonacciProducer extends Thread {

  private DataOutputStream theOutput;
  private int howMany;

  public FibonacciProducer(OutputStream out, int howMany) {
    theOutput = new DataOutputStream(out);
    this.howMany = howMany;
  }

  public void run() {

    try {
      int f1 = 1;
      int f2 = 1;
      theOutput.writeInt(f1);
      theOutput.writeInt(f2);

      // Now calculate the rest.
      for (int i = 2; i < howMany; i++) {
        int temp = f2;
        f2 = f2 + f1;
        f1 = temp;
        if (f2 < 0) { // overflow
         break;
        }
        theOutput.writeInt(f2);
      }
    }
    catch (IOException ex) { System.err.println(ex); }
  }
}
