import java.io.*;

public class RootsFile {

  public static void main(String[] args) {

    try {
      FileOutputStream fout = new FileOutputStream("roots.dat");
      DataOutputStream dout = new DataOutputStream(fout);
      for (int i = 0; i <= 1000; i++) {
        dout.writeDouble(Math.sqrt(i));
      }
      dout.flush();
      dout.close();
    }
    catch (IOException e) {
      System.err.println(e);
    }

  }

}
