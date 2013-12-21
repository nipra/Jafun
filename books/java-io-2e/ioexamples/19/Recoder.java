import java.io.*;
import java.nio.charset.*;
import java.nio.*;
import java.nio.channels.*;

public class Recoder {

  public static void main(String[] args) {
    
    if (args.length != 2) {
       System.err.println(
         "Usage: java Recoder inputEncoding outputEncoding <inFile >outFile");
       return;
    }
    
    try {
      Charset inputEncoding = Charset.forName(args[0]);
      Charset outputEncoding = Charset.forName(args[1]);
      convert(inputEncoding, outputEncoding, System.in, System.out);
    }
    catch (UnsupportedCharsetException ex) {
      System.err.println(ex.getCharsetName() + " is not supported by this VM.");
    }
    catch (IllegalCharsetNameException  ex) {
      System.err.println(
        "Usage: java Recoder inputEncoding outputEncoding <inFile >outFile");
    }
    catch (IOException ex) {
      System.err.println(ex.getMessage());
    }
  }

  private static void convert(Charset inputEncoding, Charset outputEncoding, 
    InputStream inStream, OutputStream outStream) throws IOException {
    
    ReadableByteChannel in = Channels.newChannel(inStream);
    WritableByteChannel out = Channels.newChannel(outStream);
 
    for (ByteBuffer inBuffer = ByteBuffer.allocate(4096);
         in.read(inBuffer) != -1;
         inBuffer.clear()) {
       
      inBuffer.flip();
      CharBuffer cBuffer = inputEncoding.decode(inBuffer);
      ByteBuffer outBuffer = outputEncoding.encode(cBuffer);
      while (outBuffer.hasRemaining()) out.write(outBuffer);
    }
  }
}
