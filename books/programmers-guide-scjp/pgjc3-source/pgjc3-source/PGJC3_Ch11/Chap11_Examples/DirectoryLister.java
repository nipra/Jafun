import java.io.File;
import java.io.IOException;

public class DirectoryLister {
  public static void main(String[] args) {
    if (args.length == 0) {                                         // (1)
      System.err.println("Please specify a directory name.");
      return;
    }
    File entry = new File(args[0]);                                 // (2)
    listDirectory(entry);
  }

  public static void listDirectory(File entry) {
    try {
      if (!entry.exists()) {                                        // (3)
        System.out.println(entry.getName() + " not found.");
        return;
      }
      if (entry.isFile()) {
        // Write the pathname of the entry:
        System.out.println(entry.getCanonicalPath());               // (4)
      } else if (entry.isDirectory()) {
        // Create list of entries for this directory:
        String[] entryNames = entry.list();                         // (5)
        for (String entryName : entryNames) {
          // Create a File object for each entry name:
          File thisEntry = new File(entry.getPath(), entryName);    // (6)
          // List this entry by a recursive call:
          listDirectory(thisEntry);                                 // (7)
        }
      }
    } catch(IOException e) { System.out.println("Error: " + e); }
  }
}