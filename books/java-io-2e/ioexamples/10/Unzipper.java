import java.util.*;
import java.util.zip.*;
import java.io.*;

public class Unzipper {

  public static void main(String[] args) throws IOException {
    ZipFile zf = new ZipFile(args[0]);
    Enumeration e = zf.entries();
    while (e.hasMoreElements()) {
      ZipEntry ze = (ZipEntry) e.nextElement();
      System.out.println("Unzipping " + ze.getName());
      FileOutputStream fout = new FileOutputStream(ze.getName());
      InputStream in = zf.getInputStream(ze);
      for (int c = in.read(); c != -1; c = in.read()) {
        fout.write(c);
      }
      in.close();
      fout.close();
    }
  }
}
