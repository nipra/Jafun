import javax.comm.*;
import java.util.*;
import java.io.*;

public class PortTyper {

  public static void main(String[] args) {

    if (args.length < 1) {
      System.out.println("Usage: java PortTyper portName");
      return;
    }

    try {    
      CommPortIdentifier com = CommPortIdentifier.getPortIdentifier(args[0]);
      CommPort thePort  = com.open("PortOpener", 10);
      CopyThread input = new CopyThread(System.in, thePort.getOutputStream());
      CopyThread output = new CopyThread(thePort.getInputStream(), System.out);
      input.start();
      output.start();
    }
    catch (Exception ex) {System.out.println(ex);}
  }
}

class CopyThread extends Thread {

  private InputStream theInput;
  private OutputStream theOutput;

  CopyThread(InputStream in) {
    this(in, System.out);
  }

  CopyThread(OutputStream out) {
    this(System.in, out);
  }

  CopyThread(InputStream in, OutputStream out) {
    theInput = in;
    theOutput = out;
  }

  public void run() {

    try {
      byte[] buffer = new byte[256];
      while (true) {
        int bytesRead = theInput.read(buffer);
        if (bytesRead == -1) break;
        theOutput.write(buffer, 0, bytesRead);
      }
    }
    catch (IOException ex) {System.err.println(ex);}
  }
}
