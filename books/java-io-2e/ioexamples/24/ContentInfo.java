import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import javax.microedition.io.*;
import java.io.IOException;

public class ContentInfo extends MIDlet implements CommandListener {

  private Display display;
  private TextBox textBox;

  public void startApp() {
    display = Display.getDisplay(this);

    if (textBox == null) {
      textBox = new TextBox("URL", "http://", 255, TextField.URL);
    }
    display.setCurrent(textBox);

    Command getInfo = new Command("Get Info", Command.OK, 10);
    textBox.addCommand(getInfo);
    textBox.setCommandListener(this);
  }

  public void commandAction(Command command, Displayable displayable) {
    // Network operations should not run in this same thread
    Thread t = new Thread(
       new Runnable() {
         public void run() {
           display.setCurrent(getInfo());
         }
       }
     );
     t.start();
  }

  private Form getInfo() {

    Form form = new Form("Content Info");
    ContentConnection conn = null;
    try {
      conn = (ContentConnection) Connector.open(textBox.getString());
      String type = conn.getType();
      String encoding = conn.getEncoding();
      long length = conn.getLength();
      
      form.append("Media type: " + type + "\r\n");
      if (encoding != null) form.append("Encoding: " + encoding + "\r\n");
      form.append("Length: " + String.valueOf(length));
    }
    catch (IOException ex) {
      form.append(ex.getMessage());
    }
    finally {
      try {
        if (conn != null) conn.close();
      }
      catch (IOException ex) { /* Oh well. we tried.*/ }
    }
    return form;
  }

  public void pauseApp() {}
  public void destroyApp(boolean unconditional) {}
}
