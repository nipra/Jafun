import java.io.*;

public class FibonacciConsumer extends Thread {

  private DataInputStream theInput;

  public FibonacciConsumer(InputStream in) {
    theInput = new DataInputStream(in);
  }

  public void run() {

    try {
      while (true) {
        System.out.println(theInput.readInt());
      }
    }
    catch (IOException ex) {
      if (ex.getMessage().equals("Pipe broken")
        || ex.getMessage().equals("Write end dead")) {
        // normal termination
        return;
      }
      ex.printStackTrace();
    }
  }
}
