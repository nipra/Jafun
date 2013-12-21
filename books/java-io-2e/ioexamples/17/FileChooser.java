import java.io.*;
import java.awt.*;
import com.macfaq.io.*;


public class FileTyper {

  public static void main(String[] args) {
  
    try {
      File f = getFile();
      if (f == null) return;
      FileInputStream fin = new FileInputStream(f);
      StreamCopier.copy(fin, System.out);
    }
    catch (IOException e) {
      System.err.println(e);
    }    
  
    // Work around annoying AWT non-daemon thread bug
    System.exit(0);
  
  }

  public static File getFile() throws IOException {
  
    // dummy Frame, never shown
    Frame parent = new Frame();
    FileDialog fd = new FileDialog(parent, "Please choose a file:", FileDialog.LOAD);
    fd.show();
    
    // program stops here until user selects a file or cancels
    
    String dir = fd.getDirectory();
    String file = fd.getFile();
    
    // clean up our windows, they won't be needed again
    parent.dispose();
    fd.dispose();
    
    if (dir == null || file == null) { // user cancelled the dialog
      return null;
    }
    return new File(dir, file);
    
  }

}
