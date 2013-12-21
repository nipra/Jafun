import java.io.*;
import com.elharo.io.*;

public class FileDumper {

  public static final int ASC = 0;
  public static final int DEC = 1;
  public static final int HEX = 2;

  public static void main(String[] args) {

    if (args.length < 1) {
      System.err.println("Usage: java FileDumper [-ahd] file1 file2...");
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
        if (mode == ASC) dumpAscii(args[i]);
        else if (mode == HEX) dumpHex(args[i]);
        else if (mode == DEC) dumpDecimal(args[i]);   
      }  
      catch (IOException ex) {
        System.err.println("Error reading from " + args[i] + ": " 
         + ex.getMessage());
      } 
      if (i < args.length-1) {  // more files to dump
        System.out.println("\r\n--------------------------------------\r\n");
      }
    }
  }

  public static void dumpAscii(String filename) throws IOException {

    FileInputStream fin = null;
    try {
      fin = new FileInputStream(filename);
      StreamCopier.copy(fin, System.out);
    }
    finally {
      if (fin != null) fin.close();
    }
  }

  public static void dumpDecimal(String filename) throws IOException {

    FileInputStream fin = null;
    byte[] buffer = new byte[16];
    boolean end = false;

    try {
      fin = new FileInputStream(filename);
      while (!end) {
        int bytesRead = 0;
        while (bytesRead < buffer.length) {
          int r = fin.read(buffer, bytesRead, buffer.length - bytesRead);
          if (r == -1) {
            end = true;
            break;
          }
          bytesRead += r;
        }
        for (int i = 0; i < bytesRead; i++) {
          int dec = buffer[i];
          if (dec < 0) dec = 256 + dec;
          if (dec < 10) System.out.print("00" + dec + " ");
          else if (dec < 100) System.out.print("0" + dec + " ");
          else System.out.print(dec + " ");
        }
        System.out.println();
      }
    }
    finally {
      if (fin != null) fin.close();
    }
  }

  public static void dumpHex(String filename) throws IOException {

    FileInputStream fin = null;
    byte[] buffer = new byte[24];
    boolean end = false;

    try {
      fin = new FileInputStream(filename);
      while (!end) {
        int bytesRead = 0;
        while (bytesRead < buffer.length) {
          int r = fin.read(buffer, bytesRead, buffer.length - bytesRead);
          if (r == -1) {
            end = true;
            break;
          }
          bytesRead += r;
        }
        for (int i = 0; i < bytesRead; i++) {
          int hex = buffer[i];
          if (hex < 0) hex = 256 + hex;
          if (hex >= 16) System.out.print(Integer.toHexString(hex) + " ");
          else System.out.print("0" + Integer.toHexString(hex) + " ");
        }
        System.out.println();
      }
    }
    finally {
      if (fin != null) fin.close();
    }
  }
}
