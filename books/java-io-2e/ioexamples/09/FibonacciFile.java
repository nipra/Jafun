import java.io.*;

public class FibonacciFile {

  public static void main(String args[]) throws IOException {

    int howMany = 20;
     
    // To avoid resizing the buffer, calculate the size of the
    // byte array in advance.
    ByteArrayOutputStream bout = new ByteArrayOutputStream(howMany*4);
    DataOutputStream dout = new DataOutputStream(bout);

    // First two Fibonacci numbers must be given
    // to start the process.
    int f1 = 1;
    int f2 = 1;
    dout.writeInt(f1);
    dout.writeInt(f2);

    // Now calculate the rest.
    for (int i = 3; i <= 20; i++) {
      int temp = f2;
      f2 = f2 + f1;
      f1 = temp;
      dout.writeInt(f2);
    }

    FileOutputStream fout = new FileOutputStream("fibonacci.dat");
    try {
      bout.writeTo(fout);
      fout.flush();
    }
    finally {
      fout.close();
    }
  }  
}
