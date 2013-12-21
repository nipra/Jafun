import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.zip.*;
import javax.swing.*;

public class GUIGZipper {

  public final static String GZIP_SUFFIX = ".gz";

  public static void main(String[] args) 
    throws InterruptedException, InvocationTargetException {
    
    SwingUtilities.invokeAndWait(
      new Runnable() {
        public void run() {
          JFrame parent = new JFrame();
          JFileChooser fc = new JFileChooser();
          fc.setDialogTitle("Please choose a file to gzip: ");
          fc.setApproveButtonMnemonic('g');
      
          while (true) {
            int result = fc.showDialog(parent, 
              "Select a file, then press this button to gzip it");
            if (result == JFileChooser.APPROVE_OPTION) {
              try {
                File f = fc.getSelectedFile();
                if (f == null) {
                  JOptionPane.showMessageDialog(parent, 
                    "Can only gzip files, not directories");
                }
                else {
                  InputStream in = new FileInputStream(f);
                  FileOutputStream fout = new FileOutputStream(f.getAbsolutePath() 
                   + GZIP_SUFFIX);
                  OutputStream gzout = new GZIPOutputStream(fout);
                  for (int c = in.read(); c != -1; c = in.read()) {
                    gzout.write(c);
                  }
                  // These next two should be in a finally block; but the multiple 
                  // nested try-catch blocks just got way too complicated for a
                  //  simple example
                  in.close();
                  gzout.close();
                }
              } 
              catch (IOException ex) {
                ex.printStackTrace();
              }
            }
            else {
              System.exit(0);
            } // end else
          } // end while
        }  // end run
      } // end Runnable
    ); // end invokeAndWait
  } // end main
} // end class
