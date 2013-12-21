import java.io.*;
import java.nio.channels.*;

public class NIOTransfer {

  public static void main(String[] args) throws IOException {

    FileInputStream inFile = new FileInputStream(args[0]);
    FileOutputStream outFile = new FileOutputStream(args[1]);

    FileChannel inChannel = inFile.getChannel();
    FileChannel outChannel = outFile.getChannel();    
   
    inChannel.transferTo(0, inChannel.size(), outChannel);
        
    inChannel.close();
    outChannel.close();     
  }
}
