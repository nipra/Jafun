import java.io.*;

public class SerializableRandomAccessFile extends RandomAccessFile 
 implements Serializable {

  public SerializableRandomAccessFile(String name, String mode) 
   throws IOException {
    super(name, mode);
  }
  
  public SerializableRandomAccessFile(File file, String mode) 
   throws IOException {
    super(file, mode);
  }

  public static void main(String[] args) {

    try {
      SerializableRandomAccessFile sraf = new 
       SerializableRandomAccessFile(args[0], args[1]);
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      ObjectOutputStream oos = new ObjectOutputStream(bos);
      oos.writeObject(sraf);
      oos.close();
      System.out.println("Wrote object!");

      ByteArrayInputStream bis = new 
       ByteArrayInputStream(bos.toByteArray());
      ObjectInputStream ois = new ObjectInputStream(bis);
      Object o = ois.readObject();
      System.out.println("Read object!");
    }
    catch (Exception e) {
      System.err.println(e);
    }

  }

}