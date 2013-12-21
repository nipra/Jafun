import java.io.*;
import java.util.*;

public class DirList {

  private File directory;
  private int indent = 2;
  private static List seen = new ArrayList();

  public static void main(String[] args) throws IOException {
      DirList dl = new DirList(args[0]);
      dl.list();
  }

  public DirList(String name) throws IOException {
    this(new File(name), 2);
  }
  
  public DirList(File f) throws IOException {
    this(f, 2);
  }
  
  public DirList(File directory, int indent) throws IOException {
    if (directory.isDirectory()) {
      this.directory = new File(directory.getCanonicalPath());
    }
    else {
      throw new IOException(directory.toString() + " is not a directory");
    }
    this.indent = indent;
    String spaces = "";
    for (int i = 0; i < indent-2; i++) spaces += " ";    
    System.out.println(spaces + directory + File.separatorChar);    
  }
  
  public void list() throws IOException {
  
    if (!seen.contains(this.directory)) {
      seen.add(this.directory);
      String[] files = directory.list();
      String spaces = "";
      for (int i = 0; i < indent; i++) spaces += " ";    
      for (int i = 0; i < files.length; i++) {
        File f = new File(directory, files[i]);
        if (f.isFile()) {
          System.out.println(spaces + f.getName());
        }
        else { // it's another directory
          DirList dl = new DirList(f, indent + 2);
          dl.list(); 
        }
      }
    }
  }
}
