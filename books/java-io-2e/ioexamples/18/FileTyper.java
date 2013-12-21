import java.io.*;
import java.awt.*;

public class FileTyper {

  public static void main(String[] args) throws IOException {
  
    InputStream in = null;
    try {
      File f = getFile();
      if (f == null) return;
      in = new FileInputStream(f);
      for (int c = in.read(); c != -1; c = in.read()) {
        System.out.write(c);
      }
    }
    finally {
      if (in != null) in.close();
    }
  
    // Work around annoying AWT non-daemon thread bug.
    System.exit(0);
  }

  public static File getFile() {
  
    // dummy Frame, never shown
    Frame parent = new Frame();
    FileDialog fd = new FileDialog(parent, "Please choose a file:", 
     FileDialog.LOAD);
    fd.setVisible(true);
    
    // Program stops here until user selects a file or cancels.
    String dir = fd.getDirectory();
    String file = fd.getFile();
    
    // Clean up our windows, they won't be needed again.
    parent.dispose();
    fd.dispose();
    
    if (dir == null || file == null) { // user cancelled the dialog
      return null;
    }
    return new File(dir, file);
  }
}
