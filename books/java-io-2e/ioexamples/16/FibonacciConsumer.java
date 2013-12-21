import java.io.IOException;
import java.math.BigInteger;
import java.nio.channels.*;
import java.nio.*;

public class FibonacciConsumer extends Thread{

  private ReadableByteChannel in;

  public FibonacciConsumer(ReadableByteChannel in) {
    this.in = in;
  }
  
  public void run() {

    ByteBuffer sizeb = ByteBuffer.allocate(4);
    try {
      while (sizeb.hasRemaining()) in.read(sizeb);
      sizeb.flip();
      int howMany = sizeb.getInt();
      sizeb.clear();
      
      for (int i = 0; i < howMany; i++) {
        while (sizeb.hasRemaining()) in.read(sizeb);
        sizeb.flip();
        int length = sizeb.getInt();
        sizeb.clear();
        
        ByteBuffer data = ByteBuffer.allocate(length);
        while (data.hasRemaining()) in.read(data);
        
        BigInteger result = new BigInteger(data.array());
        System.out.println(result);
      }
    }
    catch (IOException ex) {
      System.err.println(ex);
    }
    finally {
      try {
        in.close();
      } 
      catch (Exception ex) {
        // We tried
      } 
    }
  }
}
