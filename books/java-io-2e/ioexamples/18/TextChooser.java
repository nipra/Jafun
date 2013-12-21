import java.io.*;

public class TextChooser implements FilenameFilter {

  public boolean accept(File dir, String name) {
 
    if (name.endsWith(".java")) return true;
    else if (name.endsWith(".jav")) return true;
    else if (name.endsWith(".html")) return true;
    else if (name.endsWith(".htm")) return true;
    else if (name.endsWith(".txt")) return true;
    else if (name.endsWith(".text")) return true;
    return false;
  }
}
