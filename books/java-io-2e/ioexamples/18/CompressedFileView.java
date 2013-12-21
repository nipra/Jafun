import java.io.*;
import javax.swing.*;
import javax.swing.filechooser.*;

public class CompressedFileView extends FileView {

  ImageIcon zipIcon = new ImageIcon("images/zipIcon.gif");
  ImageIcon gzipIcon = new ImageIcon("images/gzipIcon.gif");
  ImageIcon deflateIcon = new ImageIcon("images/deflateIcon.gif");
    
  public String getName(File f) {
    return f.getName();
  }
    
  public String getTypeDescription(File f) {
   
    if (f.getName().endsWith(".zip")) return "Zip archive";
    if (f.getName().endsWith(".gz")) return "Gzipped file";
    if (f.getName().endsWith(".dfl")) return "Deflated file";
    return null;
  }
    
  public Icon getIcon(File f) {
  
    if (f.getName().endsWith(".zip")) return zipIcon;
    if (f.getName().endsWith(".gz")) return gzipIcon;
    if (f.getName().endsWith(".dfl")) return deflateIcon;
    return null;
  }
  
  public String getDescription(File f) {
    return null;
  }
    
  public Boolean isTraversable(File f) {
    return null; 
  }
}
