import java.io.*;

public class DoubleReader {

  public static void main(String[] args) {
  
    for (int i = 0; i < args.length; i++) {
      
      try {
        FileInputStream fin = new FileInputStream(args[i]);
        // now that we know the file exists, print its name
        System.out.println("-----------" + args[i] + "-----------");
        DataInputStream din = new DataInputStream(fin);
        while (true) {
          double theNumber = din.readDouble();
          System.out.println(theNumber);               
        }  // end while        
      } // end try
      catch (EOFException e) {
        // normal termination
      }
      catch (IOException e) {
        // abnormal termination
        System.err.println(e);
      }
      
    }  // end for
    
  }  // end main

}  // end DoubleReader
