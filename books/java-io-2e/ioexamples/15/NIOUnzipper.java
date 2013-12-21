import java.io.*;
import java.util.zip.*;
import java.nio.*;
import java.nio.channels.*;

public class NIOUnzipper {

  public static void main(String[] args) throws IOException {

    FileInputStream fin = new FileInputStream(args[0]);      
    GZIPInputStream gzin = new GZIPInputStream(fin);
    ReadableByteChannel in = Channels.newChannel(gzin);
    
    WritableByteChannel out = Channels.newChannel(System.out);
    ByteBuffer buffer = ByteBuffer.allocate(65536);
    while (in.read(buffer) != -1) {
      buffer.flip();
      out.write(buffer);
      buffer.clear();
    }
  }
}
