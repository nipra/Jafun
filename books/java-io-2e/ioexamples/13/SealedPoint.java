import java.security.*;
import java.io.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.awt.*;

public class SealedPoint {

  public static void main(String[] args) 
   throws GeneralSecurityException, IOException {
  
    Point tdp = new Point(32, 45);
    FileOutputStream fout = new FileOutputStream("point.des");
    ObjectOutputStream oout = new ObjectOutputStream(fout);
      
    // Create a key.
    byte[] desKeyData = {(byte) 0x90, (byte) 0x67, (byte) 0x3E, (byte) 0xE6, 
                         (byte) 0x42, (byte) 0x15, (byte) 0x7A, (byte) 0xA3 };
    DESKeySpec desKeySpec = new DESKeySpec(desKeyData);
    SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
    SecretKey desKey = keyFactory.generateSecret(desKeySpec);
      
    // Use Data Encryption Standard.
    Cipher des = Cipher.getInstance("DES/ECB/PKCS5Padding");
    des.init(Cipher.ENCRYPT_MODE, desKey);

    SealedObject so = new SealedObject(tdp, des);
    oout.writeObject(so);
    oout.close();
  }
}
