import java.io.*;
import java.util.zip.*;
import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import com.elharo.io.*;

public class FileDumper6 {

  public static final int ASC    = 0;
  public static final int DEC    = 1;
  public static final int HEX    = 2;
  public static final int SHORT  = 3;
  public static final int INT    = 4;
  public static final int LONG   = 5;
  public static final int FLOAT  = 6;
  public static final int DOUBLE = 7;

  public static void dump(InputStream in, Writer out, int mode, 
   boolean bigEndian, boolean deflated, boolean gzipped, String password) 
   throws IOException {
    
    // The reference variable in may point to several different objects
    // within the space of the next few lines.
    if (password != null && !password.equals("")) {
      // Create a key.
      try {
        byte[] desKeyData = password.getBytes();
        DESKeySpec desKeySpec = new DESKeySpec(desKeyData);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey desKey = keyFactory.generateSecret(desKeySpec);

        // Use Data Encryption Standard.
        Cipher des = Cipher.getInstance("DES/ECB/PKCS5Padding");
        des.init(Cipher.DECRYPT_MODE, desKey);
        
        in = new CipherInputStream(in, des);
      }
      catch (GeneralSecurityException ex) {
        throw new IOException(ex.getMessage());
      }
    }
    
    if (deflated) {
      in = new InflaterInputStream(in);
    }
    else if (gzipped) {
      in = new GZIPInputStream(in);
    }

    if (bigEndian) {
      DataInputStream din = new DataInputStream(in);
      switch (mode) {
        case HEX: 
          in = new HexFilter(in);
          break;
        case DEC: 
          in = new DecimalFilter(in);
          break;
        case INT: 
          in = new IntFilter(din);
          break;
        case SHORT: 
          in = new ShortFilter(din);
          break;
        case LONG: 
          in = new LongFilter(din);
          break;
        case DOUBLE: 
          in = new DoubleFilter(din);
          break;
        case FLOAT: 
          in = new FloatFilter(din);
          break;
        default:
      }
    }
    else {
      LittleEndianInputStream lin = new LittleEndianInputStream(in);
      switch (mode) {
        case HEX: 
          in = new HexFilter(in);
          break;
        case DEC: 
          in = new DecimalFilter(in);
          break;
        case INT: 
          in = new LEIntFilter(lin);
          break;
        case SHORT: 
          in = new LEShortFilter(lin);
          break;
        case LONG: 
          in = new LELongFilter(lin);
          break;
        case DOUBLE: 
          in = new LEDoubleFilter(lin);
          break;
        case FLOAT: 
          in = new LEFloatFilter(lin);
          break;
        default:  
      }
    }   
    for (int c = in.read(); c != -1; c = in.read()) {
      out.write(c); 
    }
    in.close();
  }

  public static void dump(InputStream in, Writer out, 
   String inputEncoding, String outputEncoding, boolean deflated, 
   boolean gzipped, String password) throws IOException {
    
    if (inputEncoding == null || inputEncoding.equals("")) {
      inputEncoding = "US-ASCII";
    }
    
    if (outputEncoding == null || outputEncoding.equals("")) {
      outputEncoding = System.getProperty("file.encoding", "8859_1");
    }
    
    // Note that the reference variable in
    // may point to several different objects
    // within the space of the next few lines.
    if (password != null && !password.equals("")) {
      try {
        // Create a key.
        byte[] desKeyData = password.getBytes();
        DESKeySpec desKeySpec = new DESKeySpec(desKeyData);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey desKey = keyFactory.generateSecret(desKeySpec);

        // Use Data Encryption Standard.
        Cipher des = Cipher.getInstance("DES/ECB/PKCS5Padding");
        des.init(Cipher.DECRYPT_MODE, desKey);
        in = new CipherInputStream(in, des);
      }
      catch (GeneralSecurityException ex) {
        throw new IOException(ex.getMessage());
      }
    }        
        
    if (deflated) {
      in = new InflaterInputStream(in);
    }
    else if (gzipped) {
      in = new GZIPInputStream(in);
    }
      
    InputStreamReader isr = new InputStreamReader(in, inputEncoding);  
        
    int c;
    while ((c = isr.read()) != -1) {
      out.write(c);
    }
  }
}
