import java.io.*;
import java.util.zip.*;

public class GZipper {

  public final static String GZIP_SUFFIX = ".gz";

  public static void main(String[] args) {

    for (int i = 0; i < args.length; i++) {
      try {
        InputStream fin = new FileInputStream(args[i]);      
        OutputStream fout = new FileOutputStream(args[i] + GZIP_SUFFIX);
        GZIPOutputStream gzout = new GZIPOutputStream(fout);
        for (int c = fin.read(); c != -1; c = fin.read()) {
          gzout.write(c);
        }
        gzout.close();
      }
      catch (IOException ex) {
        System.err.println(ex);
      }
    }
  }
}
