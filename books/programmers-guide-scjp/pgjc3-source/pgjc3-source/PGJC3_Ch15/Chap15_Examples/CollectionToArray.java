import java.util.Collection;
import java.util.HashSet;

public class CollectionToArray {
  public static void main(String[] args) {

    Collection<String> strSet = new HashSet<String>();
    strSet.add("2008"); strSet.add("2009"); strSet.add("2010");
    int n = strSet.size();

    Object[] objects = strSet.toArray();                  // (1)
//  String[] string = strList.toArray();                  // (2) Compile-time error!

    Object[] objArray = strSet.toArray(new Object[0]);                       // (3)
    System.out.println("Array size: " + objArray.length);
    System.out.println("Array type: " + objArray.getClass().getName());
    System.out.println("Actual element type: " +
                       objArray[0].getClass().getName());

    String[] strArray1 = new String[0];
    String[] strArray2 = strSet.toArray(strArray1);                          // (4)
    System.out.println("strArray1 == strArray2: " + (strArray1 == strArray2));

    String[] strArray3 = new String[n];
    String[] strArray4 = strSet.toArray(strArray3);                          // (5)
    System.out.println("strArray3 == strArray4: " + (strArray3 == strArray4));

    Integer[] intArray = strSet.toArray(new Integer[n]);  // (6) Runtime error!
  }
}