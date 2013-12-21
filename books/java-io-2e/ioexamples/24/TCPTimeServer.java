import javax.microedition.midlet.*;
import javax.microedition.io.*;
import java.io.*;
import java.util.Date;

public class TCPTimeServer extends MIDlet {    

  private ServerSocketConnection server;
  // The time protocol sets the epoch at 1900,
  // the java Date class at 1970. This number 
  // converts between them.   
  private long differenceBetweenEpochs = 2208988800L;
  
  protected void startApp() {
    try {
      server = (ServerSocketConnection) Connector.open("socket://:37");
      Runnable r = new Runnable() {
        public void run() {
          while (true) {
            try {
              StreamConnection conn = server.acceptAndOpen();
              Date now = new Date();
              long msSince1970 = now.getTime();
              long secondsSince1900 = msSince1970/1000L + differenceBetweenEpochs;
              DataOutputStream out = conn.openDataOutputStream();
              // write the low order four bytes
              out.write( (int) ((secondsSince1900 >>> 24) & 0xFFL));
              out.write( (int) ((secondsSince1900 >>> 16) & 0xFFL));
              out.write( (int) ((secondsSince1900 >>> 8) & 0xFFL));
              out.write( (int) (secondsSince1900 & 0xFFL));
              out.close();
            }
            catch (IOException ex) {
            }
          } 
        }
      };
      Thread t = new Thread(r);
      t.start();
    }
    catch (IOException ex) {
      // not much we can do about this here
    }
  }

  protected void pauseApp() {}

  protected void destroyApp(boolean unconditional) {
    try {
        server.close();
    }
    catch (IOException ex) {
        // We tried;
    }
  }
}
