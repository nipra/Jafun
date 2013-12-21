import javax.swing.*;
import java.beans.*;
import java.io.*;
import java.awt.*;
import com.macfaq.io.*;


public class TextPreview extends JTextArea 
 implements PropertyChangeListener {
 
  File selectedFile = null;
  String preview = "";
  int previewLength = 250;
  
  public TextPreview(JFileChooser fc) {
    super(10, 20);
    this.setEditable(false);
    this.setPreferredSize(new Dimension(150, 150));
    this.setLineWrap(true);
    fc.addPropertyChangeListener(this);
  }

  void loadText() {

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
      catch (IOException e) {
        // File preview is not an essential operation so 
        // we'll simply ignore the exception and return
      }
    }
    
  }

  public void propertyChange(PropertyChangeEvent e) {
  
    if (e.getPropertyName().equals(JFileChooser.SELECTED_FILE_CHANGED_PROPERTY)) {
      selectedFile = (File) e.getNewValue();
      if(isShowing()) {
        loadText();
        this.setText(preview);
      }
    }
    
  }
  
  public static void main(String[] args) {
    
    JFileChooser fc = new JFileChooser();
    fc.setAccessory(new TextPreview(fc));
    int result = fc.showOpenDialog(new JFrame());
    if (result == JFileChooser.APPROVE_OPTION) {
      try {
        File f = fc.getSelectedFile();
        if (f != null) {
          FileInputStream fin = new FileInputStream(f);
          StreamCopier.copy(fin, System.out);
          fin.close();
        }
      }
      catch (IOException e) {
        System.err.println(e);
      }
      
    }
    
    System.exit(0);

  }  
  
}
