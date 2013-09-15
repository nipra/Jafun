import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

public class ComparatorUsage {
  public static void main(String[] args) {

    // Choice of comparator.
//  Set<String> strSet = new TreeSet<String>();                             // (1a)
//  Set<String> strSet =
//       new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);                // (1b)
    Set<String> strSet =
         new TreeSet<String>(new RhymingStringComparator());                // (1c)

    // Add each command line argument to the set.
    for (String argument : args) {                                          // (2)
      strSet.add(argument);
    }
    System.out.println(strSet);                                             // (3)
  }
}

class RhymingStringComparator implements Comparator<String> {
  public int compare(String obj1, String obj2) {                            // (4)

    // (5) Create reversed versions of the strings:
    String reverseStr1 = new StringBuilder(obj1).reverse().toString();
    String reverseStr2 = new StringBuilder(obj2).reverse().toString();

    // Compare the reversed strings lexicographically.
    return reverseStr1.compareTo(reverseStr2);                              // (6)
  }
}