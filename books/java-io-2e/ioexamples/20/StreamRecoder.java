import java.io.*;

public class StreamRecoder {

  public static void main(String[] args) {
      
    if (args.length < 2) {
      System.err.println(
       "Usage: java StreamRecoder "
        + "infile_encoding outfile_encoding infile outfile");
      return;
    }
    
    InputStreamReader isr = null;
    OutputStreamWriter osw = null;
    try {
      File infile = new File(args[2]);
      File outfile = new File(args[3]);
      
      if (outfile.exists()
        && infile.getCanonicalPath().equals(outfile.getCanonicalPath())) {
        System.err.println("Can't convert file in place");
        return;
      }
    
      FileInputStream fin = new FileInputStream(infile);
      FileOutputStream fout = new FileOutputStream(outfile);
      isr = new InputStreamReader(fin, args[0]);
      osw = new OutputStreamWriter(fout, args[1]);
      
      while (true) {
        int c = isr.read();
        if (c == -1) break;  // end of stream
        osw.write(c);
      }
      osw.close();
      isr.close();
    }
    catch (IOException ex) {
      System.err.println(ex);
      ex.printStackTrace();
    }
    finally {
      if (isr != null) {
        try {
          isr.close();
        } catch (IOException ex) {
          ex.printStackTrace();
        }
      }
      if (osw != null) {
        try {
          osw.close();
        } 
        catch (IOException ex) {
          ex.printStackTrace();
        }
      }
    }
  }
}
