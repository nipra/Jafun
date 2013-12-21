import java.io.*;
import java.nio.*;
import java.nio.channels.*;

public class RootsChannel {

  final static int SIZE_OF_DOUBLE = 8;
  final static int LENGTH = 1001;
  
  public static void main(String[] args) throws IOException {

    // Put 1001 roots into a ByteBuffer via a double view buffer
    ByteBuffer data = ByteBuffer.allocate(SIZE_OF_DOUBLE * LENGTH);
    DoubleBuffer roots = data.asDoubleBuffer();
    while (roots.hasRemaining()) {
      roots.put(Math.sqrt(roots.position()));
    }
    
    // Open a channel to the file where we'll store the data
    FileOutputStream fout = new FileOutputStream("roots.dat");
    FileChannel outChannel = fout.getChannel();
    outChannel.write(data);
    outChannel.close();
  }
}
