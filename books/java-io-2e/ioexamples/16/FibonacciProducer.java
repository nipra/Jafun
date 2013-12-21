import java.io.IOException;
import java.math.BigInteger;
import java.nio.*;
import java.nio.channels.*;

public class FibonacciProducer extends Thread {

  private WritableByteChannel out;
  private int howMany;
  
  public FibonacciProducer(WritableByteChannel out, int howMany) {
    this.out = out;
    this.howMany = howMany;
  }

  public void run() {
   
    BigInteger low = BigInteger.ONE;
    BigInteger high = BigInteger.ONE;
    try {
       ByteBuffer buffer = ByteBuffer.allocate(4);
       buffer.putInt(this.howMany);
       buffer.flip();
       while (buffer.hasRemaining()) out.write(buffer);
       
       for (int i = 0; i < howMany; i++) {
        byte[] data = low.toByteArray();
        // These numbers can become arbitrarily large, and they grow
        // exponentially so no fixed size buffer will suffice.
        buffer = ByteBuffer.allocate(4 + data.length);
        
        buffer.putInt(data.length);
        buffer.put(data);
        buffer.flip();
        
        while (buffer.hasRemaining()) out.write(buffer);
        
        // find the next number in the series
        BigInteger temp = high;
        high = high.add(low);
        low = temp;
      }
      out.close();
      System.err.println("Closed");
    }
    catch (IOException ex) {
      System.err.println(ex);
    }
  }
}
