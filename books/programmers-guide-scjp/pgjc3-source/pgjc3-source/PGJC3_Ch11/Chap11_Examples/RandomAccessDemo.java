import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class RandomAccessDemo {
  static String fileName = "new-numbers.data";

  final static int INT_SIZE = 4;

  public static void main(String args[]) {
    try {
      RandomAccessDemo random = new RandomAccessDemo();
      random.createFile();
      random.readFile();
      random.extendFile();
      random.readFile();
    } catch (IOException e) {
      System.err.println(e);
    }
  }

  // Create a file with squares of numbers from 0 to 9.
  public void createFile() throws IOException {
    File dataFile = new File(fileName);
    RandomAccessFile outputFile = new RandomAccessFile(dataFile, "rw");
    for (int i = 0; i < 10; i++)
      outputFile.writeInt(i*i);
    outputFile.close();
  }

  // Read every other number from the file i.e. the squares of odd numbers
  public void readFile() throws IOException {
    File dataFile = new File(fileName);
    RandomAccessFile inputFile = new RandomAccessFile(dataFile, "r");
    System.out.println("Squares of odd numbers from the file:");
    long length = inputFile.length();
    for (int i = INT_SIZE; i < length; i += 2 * INT_SIZE) {
      inputFile.seek(i);                                           // (1)
      System.out.println(inputFile.readInt());
    }
    inputFile.close();
  }

  // Extend the file with squares from 10 to 19.
  public void extendFile() throws IOException {
    RandomAccessFile outputFile = new RandomAccessFile(fileName, "rw");
    outputFile.seek(outputFile.length());                            // (2)
    for (int i = 10; i < 20; i++)
      outputFile.writeInt(i*i);
    outputFile.close();
  }
}
