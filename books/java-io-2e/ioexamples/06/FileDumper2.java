import java.io.*;
import com.elharo.io.*;

public class FileDumper2 {

  public static final int ASC = 0;
  public static final int DEC = 1;
  public static final int HEX = 2;

  public static void main(String[] args) {

    if (args.length < 1) {
      System.err.println("Usage: java FileDumper2 [-ahd] file1 file2...");
      return;
    }

    int firstArg = 0;
    int mode = ASC;

    if (args[0].startsWith("-")) {
      firstArg = 1;
      if (args[0].equals("-h")) mode = HEX;
      else if (args[0].equals("-d")) mode = DEC;
    }

    for (int i = firstArg; i < args.length; i++) {
      try {
        InputStream in = new FileInputStream(args[i]);
        dump(in, System.out, mode);

        if (i < args.length-1) {  // more files to dump
          System.out.println();
          System.out.println("--------------------------------------");
          System.out.println();
        }
      }
      catch (IOException ex) {
        System.err.println(ex);
      }
    }
  }
  
  public static void dump(InputStream in, OutputStream out, int mode) 
   throws IOException {
  
    // The reference variable in may point to several different objects
    // within the space of the next few lines. We can attach
    // more filters here to do decompression, decryption, and more.
    
    if (mode == ASC) ; // no filter needed, just copy raw bytes
    else if (mode == HEX) in = new HexFilter(in);
    else if (mode == DEC) in = new DecimalFilter(in); 
        
    BufferedStreamCopier.copy(in, out);
    in.close();
  }
}
