import java.io.*;

import javax.swing.JFileChooser;
import javax.swing.filechooser.*;

public class GUIFileSpy {

  public static void main(String[] args) {
    File f = new File(args[0]);
    JFileChooser chooser = new JFileChooser();
    FileSystemView view = chooser.getFileSystemView();

    String name = view.getSystemDisplayName(f);
    if (view.isHiddenFile(f)) System.out.println(name + " is hidden.");
    if (view.isRoot(f)) System.out.println(name + " is a root.");
    if (view.isTraversable(f).booleanValue()) {
      System.out.println(name + " is traversable.");
    }
    System.out.println("The parent of " + name + " is " 
     + view.getParentDirectory(f));
    if (view.isFileSystem(f)) System.out.println(name + " is a regular file.");
    if (view.isFileSystemRoot(f)) System.out.println(name + " is the root.");
    if (view.isComputerNode(f)) System.out.println(name + " is the computer.");
    if (view.isDrive(f)) System.out.println(name + " is a disk.");
    if (view.isFloppyDrive(f)) System.out.println(name + " is a floppy disk.");
  }
}
