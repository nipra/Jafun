import java.io.*;
import java.util.*;
import java.util.zip.*;


public class SerializableZipFile2 implements Serializable {

  ZipFile zf;

  public SerializableZipFile2(String filename) throws IOException {
    this.zf = new ZipFile(filename);
  }
  
  public SerializableZipFile2(File file) throws IOException {
    this.zf = new ZipFile(file);
  }
  
  private void writeObject(ObjectOutputStream out) throws IOException{
    out.writeObject(zf.getName());
  }
  
  private void readObject(ObjectInputStream in) 
   throws IOException, ClassNotFoundException {
  
    String filename = (String) in.readObject();
    zf = new ZipFile(filename);
  
  }

  public ZipEntry getEntry(String name) {
    return zf.getEntry(name);
  }

  public InputStream getInputStream(ZipEntry entry) throws IOException {
    return zf.getInputStream(entry);
  }
  
  public String getName() {
    return zf.getName();
  }

  public Enumeration entries() {
    return zf.entries();
  }

  public int size() {
    return zf.size();
  }

  public void close() throws IOException {
    zf.close();
  }
  
  public static void main(String[] args) {

    try {
      SerializableZipFile2 szf = new SerializableZipFile2(args[0]);
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