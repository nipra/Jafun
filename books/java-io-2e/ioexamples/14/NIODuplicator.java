import java.io.*;
import java.nio.*;
import java.nio.channels.*;

public class NIODuplicator {

  public static void main(String[] args) throws IOException {

    FileInputStream inFile = new FileInputStream(args[0]);
    FileOutputStream outFile = new FileOutputStream(args[1]);

    FileChannel inChannel = inFile.getChannel();
    FileChannel outChannel = outFile.getChannel();    
   
    ByteBuffer buffer = ByteBuffer.allocate(1024*1024);    
    int bytesRead = 0;
    while (bytesRead >= 0 || buffer.hasRemaining()) {
        if (bytesRead != -1) bytesRead = inChannel.read(buffer);
        buffer.flip();
        outChannel.write(buffer);
        buffer.compact();
    }
        
    inChannel.close();
    outChannel.close();     
  }
}
