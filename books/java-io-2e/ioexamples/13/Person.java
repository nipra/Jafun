import java.util.*;
import java.io.*;

public class Person implements Serializable, ObjectInputValidation {

  static Map thePeople = new HashMap();

  private String name;
  private String ss;

  public Person(String name, String ss) {
    this.name = name;
    this.ss = ss;
    thePeople.put(ss, name);
  }

  private void readObject(ObjectInputStream in)
   throws IOException, ClassNotFoundException {
    in.registerValidation(this, 5);
    in.defaultReadObject();
  }

  public void validateObject() throws InvalidObjectException {
    if (thePeople.containsKey(this.ss)) {
      throw new InvalidObjectException(this.name + " already exists");
    }
    else {
      thePeople.put(this.ss, this.name);
    }
  }
  
  public String toString() {
    return this.name + "\t" + this.ss;
  }
  
  public static void main(String[] args) 
   throws IOException, ClassNotFoundException {
  
    Person p1 = new Person("Rusty", "123-45-5678");
    Person p2 = new Person("Beth",  "321-45-5678");
    Person p3 = new Person("David", "453-45-5678");
    Person p4 = new Person("David", "453-45-5678");
  
    Iterator iterator = thePeople.values().iterator();
    while (iterator.hasNext()) {
      System.out.println(iterator.next());
    }
    
    ByteArrayOutputStream bout = new ByteArrayOutputStream();
    ObjectOutputStream oout = new ObjectOutputStream(bout);
    oout.writeObject(p1);
    oout.writeObject(p2);
    oout.writeObject(p3);
    oout.writeObject(p4);
    oout.flush();
    oout.close();
    
    ByteArrayInputStream bin = new ByteArrayInputStream(bout.toByteArray());
    ObjectInputStream oin = new ObjectInputStream(bin);
    try {
      System.out.println(oin.readObject());
      System.out.println(oin.readObject());
      System.out.println(oin.readObject());
      System.out.println(oin.readObject());
    }
    catch (InvalidObjectException ex) {
      System.err.println(ex);
    }
    oin.close();

    // now empty the map and try again
    thePeople.clear();
    bin = new ByteArrayInputStream(bout.toByteArray());
    oin = new ObjectInputStream(bin);
    try {
      System.out.println(oin.readObject());
      System.out.println(oin.readObject());
      System.out.println(oin.readObject());
      System.out.println(oin.readObject());
    }
    catch (InvalidObjectException ex) {
      System.err.println(ex);
    }
    oin.close();
    
    iterator = thePeople.values().iterator();
    while (iterator.hasNext()) {
      System.out.println(iterator.next());
    }   
  }
}
