import java.net.*;
import java.io.*;
import java.security.*;

public class TrueMirror {

  public static void main(String[] args) 
    throws IOException, NoSuchAlgorithmException {

    if (args.length != 2) {
      System.err.println("Usage: java TrueMirror url1 url2");
      return;
    }

    URL source = new URL(args[0]);
    URL mirror = new URL(args[1]);
    byte[] sourceDigest = getDigestFromURL(source);
    byte[] mirrorDigest = getDigestFromURL(mirror);
    if (MessageDigest.isEqual(sourceDigest, mirrorDigest)) {
      System.out.println(mirror + " is up to date");
    }
    else {
      System.out.println(mirror + " needs to be updated");
    }
  }

  public static byte[] getDigestFromURL(URL u) 
   throws IOException, NoSuchAlgorithmException {
    
    MessageDigest md5 = MessageDigest.getInstance("MD5");
    InputStream in = u.openStream();
    byte[] data = new byte[128];
    while (true) {
      int bytesRead = in.read(data);
      if (bytesRead < 0) { // end of stream
        break;
      }
      md5.update(data, 0, bytesRead);
    }
    return md5.digest();
  }
}
