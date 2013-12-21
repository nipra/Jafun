import java.io.*;
import javax.swing.*;
import javax.swing.filechooser.*;


public class DirectoryChooser {

  public static void main(String[] args) {
    
    JFileChooser fc = new JFileChooser();
    fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    
    int result = fc.showOpenDialog(new JFrame());
    if (result == JFileChooser.APPROVE_OPTION) {
      File dir = fc.getSelectedFile();
      String[] contents = dir.list();
      for (int i = 0; i < contents.length; i++) {
        System.out.println(contents[i]);
      }
    }
    
    System.exit(0);

  }
  
}
