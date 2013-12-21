import java.io.*;
import java.lang.reflect.InvocationTargetException;
import javax.swing.*;

public class JFileTyper {

  public static void main(String[] args) 
    throws InterruptedException, InvocationTargetException {
    
    SwingUtilities.invokeAndWait(
      new Runnable() {
        public void run() {
          JFileChooser fc = new JFileChooser();
          int result = fc.showOpenDialog(new JFrame());
          if (result == JFileChooser.APPROVE_OPTION) {
            InputStream in = null;
            try {
              File f = fc.getSelectedFile();
              if (f != null) { // Make sure the user didn't choose a directory.
                in = new FileInputStream(f);
                for (int c = in.read(); c != -1; c = in.read()) {
                  System.out.write(c);
                }
              }
              in.close();
            }
            catch (IOException e) {System.err.println(e);}
          }
          System.exit(0);
        }
      }
    );
  }
}
