/* Copy a file.
   Command syntax: java CopyFile <from_file> <to_file>
 */
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

class CopyFile {
  public static void main(String[] args) {
    FileInputStream fromFile;
    FileOutputStream toFile;

    // Assign the files
    try {
      fromFile = new FileInputStream(args[0]);       // (1)
      toFile = new FileOutputStream(args[1]);        // (2)
    } catch(FileNotFoundException e) {
      System.err.println("File could not be copied: " + e);
      return;
    } catch(ArrayIndexOutOfBoundsException e) {
      System.err.println("Usage: CopyFile <from_file> <to_file>");
      return;
    }

    // Copy bytes
    try {                                            // (3)
      while (true) {
        int i = fromFile.read();
        if(i == -1) break;             // check end of file
        toFile.write(i);
      }
    } catch(IOException e) {
      System.err.println("Error reading/writing.");
    }

    // Close the files
    try {                                            // (4)
      fromFile.close();
      toFile.close();
    } catch(IOException e) {
      System.err.println("Error closing file.");
    }
  }
}