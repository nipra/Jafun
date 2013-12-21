import java.io.*;
import java.util.*;
import javax.microedition.io.*;
import javax.microedition.midlet.*;
import javax.microedition.io.file.*;
import javax.microedition.lcdui.*;

public class DirLister extends MIDlet {

  private int level = 0;
    
  public void startApp() { 
     Form form = new Form("File Roots");
     Enumeration roots = FileSystemRegistry.listRoots();
     while (roots.hasMoreElements()) {
       Object next = roots.nextElement();
       String url = "file:///" + next;
       System.out.println(url);
       try {
         FileConnection connection = (FileConnection) Connector.open(url);
         getInfo(connection, form);
       }
       catch (IOException ex) {
         form.append(ex.getMessage() +"\n");
       }
     }
     Display.getDisplay(this).setCurrent(form);
   }

  public void pauseApp() {}

  public void destroyApp(boolean condition) {
    notifyDestroyed();
  }

  private void getInfo(FileConnection connection, Form form) throws IOException { 
    if (connection.isDirectory()) form.append("------\n");
    for (int i = 0; i < level; i++) form.append(" ");
    form.append(connection.getPath() + connection.getName() + "\n");
    if (connection.isDirectory()) {
      level++;
      Enumeration list = connection.list();
      String path =  connection.getPath() + connection.getName();
      while (list.hasMoreElements()) {
        Object next = list.nextElement();
        String url = "file://" + path + next ;
        try {
          FileConnection child = (FileConnection) Connector.open(url);
          getInfo(child, form);
        }
        catch (Exception ex) {
          form.append(ex.getMessage() +"\n");
        }
      }
      level--;
    }  
  }
}
