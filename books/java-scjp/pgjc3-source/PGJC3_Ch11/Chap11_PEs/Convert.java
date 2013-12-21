//Filename: Convert.java
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class Convert {
  public static void main(String args[]) {

    String inEncoding  = "8859_1";
    String outEncoding = "UTF8";
    InputStream  inStream  = System.in;
    OutputStream outStream = System.out;

    try {
      try {
        inEncoding  = args[0];
        outEncoding = args[1];
        inStream  = new FileInputStream (args[2]);
        outStream = new FileOutputStream(args[3]);
      } catch (ArrayIndexOutOfBoundsException aioobe) {
        // Missing parameters are allowed.
      }
      BufferedReader reader = new BufferedReader(
          new InputStreamReader(inStream, inEncoding)
      );
      BufferedWriter writer = new BufferedWriter(
          new OutputStreamWriter(outStream, outEncoding)
      );
      // Transfer 512 chars at a time.
      char[] cbuf = new char[512];
      while (true) {
        int bytesLastRead = reader.read(cbuf);
        if (bytesLastRead == -1) break;
        writer.write(cbuf, 0, bytesLastRead);
        // Last two args was offset (none) and length.
      }
      reader.close();
      writer.close();
    } catch (FileNotFoundException fnfe) {
      System.err.println("File not found: " + fnfe.getLocalizedMessage());
    } catch (IOException ioe) {
      System.err.println("I/O error: " + ioe.getLocalizedMessage());
    } catch (SecurityException se) {
      System.err.println("Security Error: " + se.getLocalizedMessage());
    }
  }
}