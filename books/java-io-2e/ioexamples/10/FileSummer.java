import java.io.*;
import java.util.zip.*;

public class FileSummer {

  public static void main(String[] args) throws IOException {
     FileInputStream fin = new FileInputStream(args[0]);      
     System.out.println(args[0] + ":\t" + getCRC32(fin));
     fin.close();
  }
  
  public static long getCRC32(InputStream in) throws IOException {
  
    Checksum cs = new CRC32();
    
    // It would be more efficient to read chunks of data
    // at a time, but this is simpler and easier to understand.
    for (int b = in.read(); b != -1; b = in.read()) {
      cs.update(b);
    }
    return cs.getValue();
  }
}
