import java.io.*;

public class UnicodeBMPTable {

  public static void main(String[] args) throws IOException {
    // Use platform default with a fallback to Latin-1 if necessary
    String encoding = System.getProperty("file.encoding", "ISO-8859-1");
    String lineSeparator = System.getProperty("line.separator", "\r\n");
    
    OutputStream target = System.out;
    if (args.length > 0) target = new FileOutputStream(args[0]);
    if (args.length > 1) encoding = args[1];
    
    OutputStreamWriter out = null;
    try {
      out = new OutputStreamWriter(target, encoding); 
    }
    catch (UnsupportedEncodingException ex) {
      // use platform default encoding
      out = new OutputStreamWriter(target);
    }
    
    try {
      for (int i = Character.MIN_VALUE; i <= Character.MAX_VALUE; i++) {
        // Skip undefined code points; these are not characters
        if (!Character.isDefined(i)) continue;
        
        char c = (char) i;
        // Surrogates are not full characters so skip them;
        // this requires Java 5
        if (Character.isHighSurrogate(c) || Character.isLowSurrogate(c)) continue;
        
        out.write(i + ":\t" + c + lineSeparator);
      }
    }
    finally {
      out.close();
    }
  }
}
