import java.io.*;
import com.elharo.io.*;

public class MultiCopier {

  public static void main(String[] args) throws IOException {

    if (args.length < 2) {
      System.out.println("Usage: java MultiCopier infile outfile1 outfile2...");   
      return;
    }

    FileInputStream fin = new FileInputStream(args[0]);
    FileOutputStream fout1 = new FileOutputStream(args[1]);
    MultiOutputStream mout = new MultiOutputStream(fout1);
    for (int i = 2; i < args.length; i++) {
      mout.addOutputStream(new FileOutputStream(args[i]));      
    }
    BufferedStreamCopier.copy(fin, mout);
    fin.close();
    mout.close();
  }
}
