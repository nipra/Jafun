import java.util.*;
import java.util.zip.*;
import java.io.*;

public class ZipLister {

  public static void main(String[] args) throws IOException {
    ZipFile zf = new ZipFile(args[0]);
    Enumeration e = zf.entries();
    while (e.hasMoreElements()) {
      System.out.println(e.nextElement());
    } 
  }
}
