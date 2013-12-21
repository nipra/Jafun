import java.io.*;
import java.util.zip.*;

public class ExternalizableZipFile extends ZipFile 
 implements Externalizable {

  public ExternalizableZipFile(String filename) throws IOException {
    super(filename);
  }
  
  public ExternalizableZipFile(File file) throws IOException {
    super(file);
  }

  public void writeExternal(ObjectOutput out) throws IOException {
  
  }
  
  public void readExternal(ObjectInput in) 
   throws IOException, ClassNotFoundException {
   
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
    catch (Exception e) {
      e.printStackTrace();
    }

  }

}