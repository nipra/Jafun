import java.io.*;
import java.util.zip.*;

public class FileDeflater {

  public final static String DEFLATE_SUFFIX = ".dfl";

  public static void main(String[] args) {

    for (int i = 0; i < args.length; i++) {
      try {
        FileInputStream fin = new FileInputStream(args[i]);      
        FileOutputStream fout = new FileOutputStream(args[i] + DEFLATE_SUFFIX);
        DeflaterOutputStream dos = new DeflaterOutputStream(fout);
        for (int c = fin.read(); c != -1; c = fin.read()) {
            dos.write(c);
        }
        dos.close();
        fin.close();
      }
      catch (IOException ex) {
        System.err.println(ex);
      }
    }
  }
}
