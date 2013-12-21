import java.io.*;

public class JavaFilter extends javax.swing.filechooser.FileFilter {

  public boolean accept(File f) {
    if (f.getName().endsWith(".java")) return true;
    else if (f.getName().endsWith(".jav")) return true;
    else if (f.isDirectory()) return true;
    return false;
  }
  
  public String getDescription() {
    return "Java source code (*.java)";
  }
}
