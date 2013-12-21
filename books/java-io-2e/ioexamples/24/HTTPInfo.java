import java.io.IOException;
import javax.microedition.io.*;
import javax.microedition.lcdui.*;
import javax.microedition.midlet.*;

public class HTTPInfo extends MIDlet implements CommandListener {

  private Display display;
  private TextBox textBox;
  
  private Form getInfo(String url) {
    Form form = new Form("HTTP Info");
    HttpConnection connection = null;
    try {
        connection = (HttpConnection) Connector.open(url);
        connection.setRequestMethod("HEAD");
        for (int i = 0; ; i++) {
            String key = connection.getHeaderFieldKey(i);
            String value = connection.getHeaderField(i);
            if (value == null) break;
            if (key != null) form.append(key + ": " + value + "\n");
            else form.append("***" + value + "\n");;
        }
    }
    catch (Exception ex) {
       form.append(ex.getMessage() +"\n");
    }
   finally {
      try {
        if (connection != null) connection.close();
      }
      catch (IOException ex) { /* Oh well. we tried.*/ }
    }
 
    return form;
  }

  public void startApp() {
    display = Display.getDisplay(this);

    if (textBox == null) {
      textBox = new TextBox("URL", "http://", 255, TextField.URL);
    }
    display.setCurrent(textBox);

    Command getInfo = new Command("HTTP Headers", Command.OK, 10);
    textBox.addCommand(getInfo);
    textBox.setCommandListener(this);
  }

  public void commandAction(Command command, Displayable displayable) {
    Thread t = new Thread(
       new Runnable() {
         public void run() {
           display.setCurrent(getInfo(textBox.getString()));
         }
       }
     );
     t.start();
  }

  protected void pauseApp() {}
  protected void destroyApp(boolean unconditional) {}
}
