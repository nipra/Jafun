import java.io.*;
import javax.swing.JFileChooser;
import javax.swing.filechooser.*;

public class FileSystemViewer {

  public static void main(String[] args) {
    
    JFileChooser chooser = new JFileChooser();
    FileSystemView view = chooser.getFileSystemView();
    
    System.out.println("The home directory is " + view.getHomeDirectory());
    System.out.println("The default directory is " + view.getDefaultDirectory());
    System.out.println("The roots of this filesystem are: ");
    File[] roots = view.getRoots();
    for (int i = 0; i < roots.length; i++) {
      System.out.println("  " + roots[i]);
    }
  }
}
