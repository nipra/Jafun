import java.io.*;

public class File1000 {

  public static void main(String args[]) {
  
    DataOutputStream dos = null;
  
    try {
      dos = new DataOutputStream(new FileOutputStream("1000.dat"));
      for (int i = 1; i <= 1000; i++) {
        dos.writeInt(i);
      }    
    }
    catch (IOException ex) {System.err.println(ex);}
    finally {
      try { if (dos != null) dos.close(); }
      catch (IOException ex) { /* Not much else we can do */ }    
    }
  }
}
