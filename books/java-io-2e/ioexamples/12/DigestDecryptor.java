import java.io.*;
import java.security.*;
import java.security.spec.*;
import javax.crypto.*;
import javax.crypto.spec.*;

public class DigestDecryptor {

  public static void main(String[] args)
   throws IOException, GeneralSecurityException {

    if (args.length != 3) {
      System.err.println("Usage: java DigestDecryptor infile outfile password");
      return;
    }

    String infile = args[0];
    String outfile = args[1];
    String password = args[2];

    if (password.length() < 8 ) {
      System.err.println("Password must be at least eight characters long");
    }
    
    FileInputStream fin = new FileInputStream(infile);
    FileOutputStream fout = new FileOutputStream(outfile);

    // Get the digest.
    FileInputStream digestIn = new FileInputStream(infile + ".digest");
    DataInputStream dataIn = new DataInputStream(digestIn);
    // SHA digests are always 20 bytes long .
    byte[] oldDigest = new byte[20];
    dataIn.readFully(oldDigest);
    dataIn.close();

    // Create a key.
    byte[] desKeyData = password.getBytes();
    DESKeySpec desKeySpec = new DESKeySpec(desKeyData);
    SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
    SecretKey desKey = keyFactory.generateSecret(desKeySpec);

    // Use Data Encryption Standard.
    Cipher des = Cipher.getInstance("DES/ECB/PKCS5Padding");
    des.init(Cipher.DECRYPT_MODE, desKey);
    CipherOutputStream cout = new CipherOutputStream(fout, des);

    // Use SHA digest algorithm.
    MessageDigest sha = MessageDigest.getInstance("SHA");
    DigestInputStream din = new DigestInputStream(fin, sha);

    byte[] input = new byte[64];
    while (true) {
      int bytesRead = din.read(input);
      if (bytesRead == -1) break;
      cout.write(input, 0, bytesRead);
    }
    
    byte[] newDigest = sha.digest();
    if (!MessageDigest.isEqual(newDigest, oldDigest)) {
      System.out.println("Input file appears to be corrupt!");
    }

    din.close();
    cout.flush();
    cout.close();
  }
}
