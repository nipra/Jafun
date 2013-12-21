import java.io.*;
import java.util.zip.*;

public class GUnzipper {

  public static void main(String[] args) {

    for (int i = 0; i < args.length; i++) {
      if (args[i].toLowerCase().endsWith(GZipper.GZIP_SUFFIX)) {
        try {
          FileInputStream fin = new FileInputStream(args[i]);      
          GZIPInputStream gzin = new GZIPInputStream(fin);
          FileOutputStream fout = new FileOutputStream(
           args[i].substring(0, args[i].length()-3));
          for (int c = gzin.read(); c != -1; c = gzin.read()) {
            fout.write(c);
          }
          fout.close();
        }
        catch (IOException ex) {System.err.println(ex);}
      }
      else {
        System.err.println(args[i] + " does not appear to be a gzipped file.");
      }
    }
  }
}
