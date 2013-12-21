import java.io.*;
import java.util.jar.*;

public class Packer200 {

  public static void main(String[] args) {
    
    OutputStream out = null;
    try {
      JarFile f = new JarFile(args[0]);
      Pack200.Packer packer = Pack200.newPacker();
      out = new FileOutputStream(args[0] + ".pack");
      packer.pack(f, out);
    } 
    catch (IOException ex) {
      ex.printStackTrace();
    }
    finally {
      if (out != null) {
        try {
          out.close();
        } 
        catch (IOException ex) {
          System.err.println("Error closing file: " + ex.getMessage());
        }
      }
    }
  }
}
