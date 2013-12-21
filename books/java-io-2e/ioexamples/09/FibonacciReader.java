import java.io.*;

public class FibonacciReader extends Thread {

  DataInputStream theInput;

  public FibonacciReader(InputStream in) 
   throws IOException {
    theInput = new DataInputStream(in);
  }

  public void run() {

    try {
      while (true) {
        System.out.println(theInput.readInt());
      }
    }
    catch (IOException e) {
      // probably just an end of stream exception
    }

  }

}
