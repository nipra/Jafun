import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.security.SecureRandom;

public class SecureDelete {

   public static void main(String[] args) throws IOException {
    
     File file = new File(args[0]);
     if (file.exists()) {
     SecureRandom random = new SecureRandom();
     RandomAccessFile raf = new RandomAccessFile(file, "rw");
     FileChannel channel = raf.getChannel();
     MappedByteBuffer buffer 
       = channel.map(FileChannel.MapMode.READ_WRITE, 0, raf.length());
     // overwrite with zeros
     while (buffer.hasRemaining()) {
       buffer.put((byte) 0);
     }
     buffer.force();
     buffer.rewind();
     // overwrite with ones
     while (buffer.hasRemaining()) {
       buffer.put((byte) 0xFF);
     }
     buffer.force();
     buffer.rewind();
     // overwrite with random data; one byte at a time
     byte[] data = new byte[1];
     while (buffer.hasRemaining()) {
       random.nextBytes(data);
       buffer.put(data[0]);
     }
     buffer.force();
     file.delete(); 
     }
  }  
}
