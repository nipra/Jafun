import java.io.*;

public class BufferedFileCopier {

  public static void main(String[] args) {

    try {
      copy(new FileInputStream(args[0]), new FileOutputStream(args[1]));
    }
    catch (IOException ex) {
      System.err.println(ex);
    }
  }

  public static void copy(InputStream in, OutputStream out) 
   throws IOException {

    BufferedInputStream bin = new BufferedInputStream(in, 1024*1024);
    BufferedOutputStream bout = new BufferedOutputStream(out, 1024*1024);

    while (true) {
      int datum = bin.read();
      if (datum == -1) break;
      bout.write(datum);
    }
    bout.flush();
  }
}
