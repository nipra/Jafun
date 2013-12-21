import java.net.*;
import java.io.*;
import java.security.*;
import java.math.*;

public class URLDigest {

  public static void main(String[] args) 
   throws IOException, NoSuchAlgorithmException {

    URL u = new URL(args[0]);
    InputStream in = u.openStream();
    MessageDigest sha = MessageDigest.getInstance("SHA");
    byte[] data = new byte[128];
    while (true) {
      int bytesRead = in.read(data);
      if (bytesRead < 0) break;
      sha.update(data, 0, bytesRead);
    }
    byte[] result = sha.digest();
    for (int i = 0; i < result.length; i++) {
      System.out.print(result[i] + " ");
    }
    System.out.println();
    System.out.println(new BigInteger(result));
  }
}
