package pgjc3e.serialpkg3;

import java.io.*;

public class SerialInheritance {
  public static void main(String[] args)
                     throws IOException, ClassNotFoundException {
    SerialInheritance demo = new SerialInheritance();
    demo.writeData();
    demo.readData();
  }

  void writeData() throws IOException {                         // (1)
    // Set up the output stream:
    FileOutputStream outputFile = new FileOutputStream("storage.dat");
    ObjectOutputStream outputStream = new ObjectOutputStream(outputFile);

    // Write the data:
    Student student = new Student("Pendu", 1007);
    System.out.println("Before writing: " + student);
    outputStream.writeObject(student);

    // Close the stream:
    outputStream.flush();
    outputStream.close();
  }

  void readData() throws IOException, ClassNotFoundException {   // (2)
    // Set up the input stream:
    FileInputStream inputFile = new FileInputStream("storage.dat");
    ObjectInputStream inputStream = new ObjectInputStream(inputFile);

    // Read data.
    Student student = (Student) inputStream.readObject();

    // Write data on standard output stream.
    System.out.println("After reading: " + student);

    // Close the stream.
    inputStream.close();
  }
}