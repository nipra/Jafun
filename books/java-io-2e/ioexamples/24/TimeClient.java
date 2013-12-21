import java.io.IOException;
import java.util.*;
import javax.microedition.io.*;
import javax.microedition.lcdui.*;
import javax.microedition.midlet.*;

public class TimeClient extends MIDlet {
 
    private Form form;
    private final String server = "datagram://time-a.nist.gov:37";
    
    public TimeClient() {   
      form = new Form("TimeClient");
      form.append("The time is\n");
      Display.getDisplay(this).setCurrent(form);
    }  
    
    protected void startApp() {
      Timer timer = new Timer();
      TimerTask task = new TimerTask() {
        public void run() {
          form.append(new Date().toString());
          destroyApp(true);
          TimeClient.this.notifyDestroyed();
        }
      };
      timer.schedule(task, 60000); // 60 seconds from now
        
      byte[] ping = {(byte) 50}; // any byte will do
      DatagramConnection connection = null;
      try {      
        connection = (DatagramConnection) Connector.open(server);
        Datagram dgram = connection.newDatagram(ping, ping.length);
        Datagram response = connection.newDatagram(4);
      
        connection.send(dgram);
        connection.receive(response);
        byte[] result = response.getData();

        if (result.length != 4) {
          form.append("Unrecognized response format");
          return;         
        }
    
        long differenceBetweenEpochs = 2208988800L;
        long secondsSince1900 = 0;
        for (int i = 0; i < 4; i++) {
          secondsSince1900 = (secondsSince1900 << 8) | (result[i] & 0x000000FF);
        }

        long secondsSince1970 
          = secondsSince1900 - differenceBetweenEpochs;       
        long msSince1970 = secondsSince1970 * 1000;
        Date time = new Date(msSince1970);
        form.append(time.toString() + "\n");

      }
      catch (IOException ex) {
          Alert alert = new Alert("UDP Error");
          alert.setTimeout(Alert.FOREVER);
          alert.setString(ex.getMessage());
          Display.getDisplay(this).setCurrent(alert, form);
      }
      finally {
          timer.cancel();
          try {
            if (connection != null) connection.close();
          }
          catch (IOException ex) {
          }
      }
    }
    
    protected void pauseApp() {}
    protected void destroyApp(boolean unconditional)  {}
}
