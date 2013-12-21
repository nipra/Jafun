import java.io.*;

public class IntReader {

  public static void main(String[] args) throws IOException {

    DataInputStream din = null;
    try {
      FileInputStream fin = new FileInputStream(args[0]);
      System.out.println("-----------" + args[0] + "-----------");
      din = new DataInputStream(fin);
      while (true) {
        int theNumber = din.readInt();
        System.out.println(theNumber);               
      }  // end while 
    } // end try
    catch (EOFException ex) {
      // normal termination
      din.close();
    }
    catch (IOException ex) {
      // abnormal termination
      System.err.println(ex);
    }
  }  // end main
}  // end IntReader
