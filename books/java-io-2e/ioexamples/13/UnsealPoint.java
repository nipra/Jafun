import java.security.*;
import java.io.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.awt.*;

public class UnsealPoint {

  public static void main(String[] args) 
    throws IOException, GeneralSecurityException, ClassNotFoundException {
  
    FileInputStream fin = new FileInputStream("point.des");
    ObjectInputStream oin = new ObjectInputStream(fin);
      
    // Create a key.
    byte[] desKeyData = {(byte) 0x90, (byte) 0x67, (byte) 0x3E, (byte) 0xE6, 
                         (byte) 0x42, (byte) 0x15, (byte) 0x7A, (byte) 0xA3 };
    DESKeySpec desKeySpec = new DESKeySpec(desKeyData);
    SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
    SecretKey desKey = keyFactory.generateSecret(desKeySpec);
      
    SealedObject so = (SealedObject) oin.readObject();
      
    Point p = (Point) so.getObject(desKey);
    System.out.println(p);
    oin.close();
  }
}
