import java.util.zip.*;

public class SerializableZipFileNot extends ZipFile 
 implements Serializable {

  public SerializableZipFileNot(String filename) throws IOException {
    super(filename);
  }
  
  public SerializableZipFileNot(File file) throws IOException {
    super(file);
  }

  public static void main(String[] args) {

    try {
      SerializableZipFileNot szf = new SerializableZipFileNot(args[0]);
      ByteArrayOutputStream bout = new ByteArrayOutputStream();
      ObjectOutputStream oout = new ObjectOutputStream(bout);
      oout.writeObject(szf);
      oout.close();
      System.out.println("Wrote object!");

      ByteArrayInputStream bin = new 
       ByteArrayInputStream(bout.toByteArray());
      ObjectInputStream oin = new ObjectInputStream(bin);
      Object o = oin.readObject();
      System.out.println("Read object!");
    }
    catch (Exception ex) {ex.printStackTrace();}
  }
}
