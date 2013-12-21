import java.lang.reflect.InvocationTargetException;
import javax.swing.*;

public class JavaChooser {

  public static void main(String[] args) 
    throws InterruptedException, InvocationTargetException {
    
    SwingUtilities.invokeAndWait(
      new Runnable() {
        public void run() {
          JFileChooser fc = new JFileChooser();
          fc.addChoosableFileFilter(new JavaFilter());
          int result = fc.showOpenDialog(new JFrame());
          if (result == JFileChooser.APPROVE_OPTION) {
            try {
              File f = fc.getSelectedFile();
              if (f != null) {
                InputStream in = new FileInputStream(f);
                for (int c = in.read(); c != -1; c = in.read()) {
                  System.out.write(c);
                }
                in.close();
              }
            }
            catch (IOException ex) {System.err.println(ex);}
          }
          
          System.exit(0);
        }  // end run
      } // end Runnable
    ); // end invokeAndWait
  } // end main  
}  // end class
