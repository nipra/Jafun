import java.io.*;
import java.lang.reflect.InvocationTargetException;
import javax.swing.*;

public class DirectoryLister {

  public static void main(String[] args) 
    throws InterruptedException, InvocationTargetException {
    
    SwingUtilities.invokeAndWait(
      new Runnable() {
        public void run() {
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
    );
  }
}
