import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import javax.microedition.io.*;
import java.io.*;

public class Daytime extends MIDlet {    

  public Daytime() {
        
    Form form = new Form("Network Time");
    InputStream in = null;
    try {
      in = Connector.openInputStream("socket://time-a.nist.gov:13");
      StringBuffer sb = new StringBuffer();
      for (int c = in.read(); c != -1; c = in.read()) {
        sb.append((char) c);
      }
      form.append(sb.toString());
    }
    catch (IOException ex) {
      form.append(ex.getMessage());
    }
    finally {
      try {
        if (in != null) in.close();
      }
      catch (IOException ex) { /* Oh well. we tried.*/ }
    }
 
    Display.getDisplay(this).setCurrent(form);
  }

  public void startApp() {}
  public void pauseApp() {}
  public void destroyApp(boolean unconditional) {}
}
