import com.elharo.io.*;
import java.io.*;

public class StringExtractor {

  public static void main(String[] args) {
    
    if (args.length < 1) {
      System.out.println("Usage: java StringExtractor inFile");
      return;
    }
    try {
      InputStream in = new FileInputStream(args[0]);
      OutputStream out;
      if (args.length >= 2) {
        out = new FileOutputStream(args[1]);
      }
      else out = System.out;
      
      // Here's where the output stream is chained
      // to the ASCII output stream.
      PrintableOutputStream pout = new PrintableOutputStream(out);
      for (int c = in.read(); c != -1; c = in.read()) {
          pout.write(c);
      }
      out.close();
    }
    catch (FileNotFoundException e) {
      System.out.println("Usage: java StringExtractor inFile outFile");  
    }
    catch (IOException ex) {
      System.err.println(ex);
    }
  }
}
