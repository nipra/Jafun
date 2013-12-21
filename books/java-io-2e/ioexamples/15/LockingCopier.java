import java.io.*;
import java.nio.channels.*;

public class LockingCopier {

  public static void main(String[] args) throws IOException {

    FileInputStream inFile = new FileInputStream(args[0]);
    FileOutputStream outFile = new FileOutputStream(args[1]);

    FileChannel inChannel = inFile.getChannel();
    FileChannel outChannel = outFile.getChannel();    
   
    FileLock outLock = outChannel.lock();
    FileLock inLock  = inChannel.lock(0, inChannel.size(), true);
    
    inChannel.transferTo(0, inChannel.size(), outChannel);

    outLock.release();
    inLock.release();
    
    inChannel.close();
    outChannel.close();
  }
}
