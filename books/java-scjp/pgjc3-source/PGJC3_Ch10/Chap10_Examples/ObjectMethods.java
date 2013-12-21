class MyClass implements Cloneable {

  public Object clone() {
    Object obj = null;
    try { obj = super.clone();}          // Calls overridden method.
    catch (CloneNotSupportedException e) { System.out.println(e);}
    return obj;
  }
}
//______________________________________________________________________________
public class ObjectMethods {
  public static void main(String[] args) {
    // Two objects of MyClass.
    MyClass obj1 = new MyClass();
    MyClass obj2 = new MyClass();

    // Two strings.
    String str1 = new String("WhoAmI");
    String str2 = new String("WhoAmI");

    // Method hashCode() overridden in String class.
    // Strings that are equal have the same hash code.
    System.out.println("hash code for str1: " + str1.hashCode());
    System.out.println("hash code for str2: " + str2.hashCode() + "\n");

    // Hash codes are different for different MyClass objects.
    System.out.println("hash code for MyClass obj1: " + obj1.hashCode());
    System.out.println("hash code for MyClass obj2: " + obj2.hashCode()+"\n");

    // Method equals() overridden in the String class.
    System.out.println("str1.equals(str2): " + str1.equals(str2));
    System.out.println("str1 == str2:      " + (str1 == str2) + "\n");

    // Method equals() from the Object class called.
    System.out.println("obj1.equals(obj2): " + obj1.equals(obj2));
    System.out.println("obj1 == obj2:      " + (obj1 == obj2) + "\n");

    // The runtime object that represents the class of an object.
    Class rtStringClass  = str1.getClass();
    Class rtMyClassClass = obj1.getClass();
    // The name of the class represented by the runtime object.
    System.out.println("Class for str1: " + rtStringClass);
    System.out.println("Class for obj1: " + rtMyClassClass + "\n");

    // The toString() method is overridden in the String class.
    String textRepStr = str1.toString();
    String textRepObj = obj1.toString();
    System.out.println("Text representation of str1: " + textRepStr);
    System.out.println("Text representation of obj1: " + textRepObj + "\n");

    // Shallow copying of arrays.
    MyClass[] array1 = {new MyClass(), new MyClass(), new MyClass()};
    MyClass[] array2 = array1.clone();
    // Array objects are different, but share the element objects.
    System.out.println("array1 == array2:        " + (array1 == array2));
    for(int i = 0; i < array1.length; i++) {
      System.out.println("array1[" + i + "] == array2[" + i + "] : " +
                         (array1[i] == array2[i]));
    }
    System.out.println();

    // Clone an object of MyClass.
    MyClass obj3 = (MyClass) obj1.clone(); // Cast required.
    System.out.println("hash code for MyClass obj3: " + obj3.hashCode());
    System.out.println("obj1 == obj3: " + (obj1 == obj3));
  }
}