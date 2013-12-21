import java.io.*;
import java.nio.*;
import java.nio.channels.*;

public class NIOCopier {

  public static void main(String[] args) throws IOException {

    FileInputStream inFile = new FileInputStream(args[0]);
    FileOutputStream outFile = new FileOutputStream(args[1]);

    FileChannel inChannel = inFile.getChannel();
    FileChannel outChannel = outFile.getChannel();    
   
    for (ByteBuffer buffer = ByteBuffer.allocate(1024*1024);
         inChannel.read(buffer) != -1;
         buffer.clear()) {
       buffer.flip();
       while (buffer.hasRemaining()) outChannel.write(buffer);
    }
        
    inChannel.close();
    outChannel.close();     
  }
}
