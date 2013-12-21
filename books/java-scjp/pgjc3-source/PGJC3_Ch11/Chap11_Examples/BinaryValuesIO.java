import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class BinaryValuesIO {
  public static void main(String[] args) throws IOException {
    // Create a FileOutputStream.
    FileOutputStream outputFile = new FileOutputStream("primitives.data");

    // Create a DataOutputStream which is chained to the FileOutputStream.
    DataOutputStream outputStream = new DataOutputStream(outputFile);

    // Write Java primitive values in binary representation:
    outputStream.writeBoolean(true);
    outputStream.writeChar('A');               // int written as Unicode char
    outputStream.writeByte(Byte.MAX_VALUE);    // int written as 8-bits byte
    outputStream.writeShort(Short.MIN_VALUE);  // int written as 16-bits short
    outputStream.writeInt(Integer.MAX_VALUE);
    outputStream.writeLong(Long.MIN_VALUE);
    outputStream.writeFloat(Float.MAX_VALUE);
    outputStream.writeDouble(Math.PI);

    // Close the output stream, which also closes the underlying stream.
    outputStream.flush();
    outputStream.close();

    // Create a FileInputStream.
    FileInputStream inputFile = new FileInputStream("primitives.data");

    // Create a DataInputStream which is chained to the FileInputStream.
    DataInputStream inputStream = new DataInputStream(inputFile);

    // Read the binary representation of Java primitive values
    // in the same order they were written out:
    boolean v = inputStream.readBoolean();
    char    c = inputStream.readChar();
    byte    b = inputStream.readByte();
    short   s = inputStream.readShort();
    int     i = inputStream.readInt();
    long    l = inputStream.readLong();
    float   f = inputStream.readFloat();
    double  d = inputStream.readDouble();

    // Check for end of stream:
    try {
      int value = inputStream.readByte();
      System.out.println("More input: " + value);
    } catch (EOFException eofe) {
      System.out.println("End of stream");
    } finally {
      // Close the input stream, which also closes the underlying stream.
      inputStream.close();
    }

    // Write the values read to the standard input stream:
    System.out.println("Values read:");
    System.out.println(v);
    System.out.println(c);
    System.out.println(b);
    System.out.println(s);
    System.out.println(i);
    System.out.println(l);
    System.out.println(f);
    System.out.println(d);
  }
}