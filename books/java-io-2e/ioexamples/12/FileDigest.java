import java.net.*;
import java.io.*;
import java.security.*;

public class FileDigest {

  public static void main(String[] args)
   throws IOException, NoSuchAlgorithmException {

    if (args.length != 2) {
      System.err.println("Usage: java FileDigest url filename");
      return;
    }

    URL u = new URL(args[0]);
    FileOutputStream out = new FileOutputStream(args[1]);
    copyFileWithDigest(u.openStream(), out);
    out.close();
  }

  public static void copyFileWithDigest(InputStream in, OutputStream out) 
   throws IOException, NoSuchAlgorithmException {
    
     MessageDigest sha = MessageDigest.getInstance("SHA-512");
     DigestOutputStream dout = new DigestOutputStream(out, sha);
     byte[] data = new byte[128];
     while (true) {
       int bytesRead = in.read(data);
       if (bytesRead < 0) break;
       dout.write(data, 0, bytesRead);
     }
     dout.flush();
     byte[] result = dout.getMessageDigest().digest();
     for (int i = 0; i < result.length; i++) {
       System.out.print(result[i] + " ");
     }
     System.out.println();
  }
}
