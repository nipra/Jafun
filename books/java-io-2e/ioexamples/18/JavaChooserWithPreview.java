import java.io.*;
import javax.swing.*;
import javax.swing.filechooser.*;
import com.macfaq.swing.filechooser.*;
import com.macfaq.io.*;


public class JavaChooserWithPreview {

  public static void main(String[] args) {
    
    JFileChooser fc = new JFileChooser();
    fc.addChoosableFileFilter(new JavaFilter());
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