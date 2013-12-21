package pgjc3e.serialpkg2;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SerialClient {

  public static void main(String args[])
                     throws IOException, ClassNotFoundException {
    SerialClient demo = new SerialClient();
    demo.writeData();
    demo.readData();
  }

  void writeData() throws IOException {                            // (4)
    // Set up the output stream:
    FileOutputStream outputFile = new FileOutputStream("storage.dat");
    ObjectOutputStream outputStream = new ObjectOutputStream(outputFile);

    // Write the data:
    Wheel wheel = new Wheel(65);
    Unicycle uc = new Unicycle(wheel);
    System.out.println("Before writing: " + uc);
    outputStream.writeObject(uc);

    // Close the stream:
    outputStream.flush();
    outputStream.close();
  }

  void readData() throws IOException, ClassNotFoundException {     // (5)
    // Set up the input streams:
    FileInputStream inputFile = new FileInputStream("storage.dat");
    ObjectInputStream inputStream = new ObjectInputStream(inputFile);

    // Read data.
    Unicycle uc = (Unicycle) inputStream.readObject();

    // Write data on standard output stream.
    System.out.println("After reading: " + uc);

    // Close the stream.
    inputStream.close();
  }
}