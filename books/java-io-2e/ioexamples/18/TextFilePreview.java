import javax.swing.*;
import java.beans.*;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.awt.*;

public class TextFilePreview extends JTextArea 
 implements PropertyChangeListener {
 
  private File selectedFile = null;
  private String preview = "";
  private int previewLength = 250;
  
  public TextFilePreview(JFileChooser fc) {
    super(10, 20);
    this.setEditable(false);
    this.setPreferredSize(new Dimension(150, 150));
    this.setLineWrap(true);
    fc.addPropertyChangeListener(this);
  }

  private void loadText() {

    if (selectedFile != null) {
      try {
        FileInputStream fin = new FileInputStream(selectedFile);
        byte[] data = new byte[previewLength];
        int bytesRead = 0;
        for (int i = 0; i < previewLength; i++) {
          int b = fin.read();
          if (b == -1) break;
          bytesRead++;
          data[i] = (byte) b;
        }
        preview = new String(data, 0, bytesRead);
        fin.close();
      }
      catch (IOException ex) {
        // File preview is not an essential operation so 
        // we'll simply ignore the exception and return.
      }
    }
  }

  public void propertyChange(PropertyChangeEvent evt) {
  
    if (evt.getPropertyName().equals(
     JFileChooser.SELECTED_FILE_CHANGED_PROPERTY)) {
      selectedFile = (File) evt.getNewValue();
      if(isShowing()) {
        loadText();
        this.setText(preview);
      }
    }
  }
  
  public static void main(String[] args) 
    throws InterruptedException, InvocationTargetException {
    
    SwingUtilities.invokeAndWait(
      new Runnable() {
        public void run() {
          JFileChooser fc = new JFileChooser();
          fc.setAccessory(new TextFilePreview(fc));
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
  }  
}
