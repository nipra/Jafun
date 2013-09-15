import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import static java.lang.System.out;

public class TestCaseVNO {
  /**  Type parameter N represents a class implementing a version number. */
  public static <N> void test(                                         // (1)
                 N latest,                                             // (2a)
                 N inShops,                                            // (2b)
                 N older,                                              // (2c)
                 N[] versions,                                         // (3)
                 Integer[] downloads) {                                // (4)

    // Print the class name.
    out.println(latest.getClass());                                    // (5)

    // Various tests.
    out.println("Test object reference and value equality:");
    out.printf ("    latest: %s, inShops: %s, older: %s%n" ,
                     latest, inShops, older);
    out.println("    latest == inShops:      " + (latest == inShops)); // (6)
    out.println("    latest.equals(inShops): " +
                     (latest.equals(inShops)));                        // (7)
    out.println("    latest == older:        " + (latest == older));   // (8)
    out.println("    latest.equals(older):   " + latest.equals(older));// (9)

    N searchKey = inShops;                                             // (10)
    boolean found = false;
    for (N version : versions) {
      found = searchKey.equals(version);                               // (11)
      if (found) break;
    }
    out.println("Array: " + Arrays.toString(versions));                // (12)
    out.println("    Search key " + searchKey + " found in array: " +
                found);                                                // (13)

    List<N> vnoList = Arrays.asList(versions);                         // (14)
    out.println("List: " + vnoList);
    out.println("    Search key " + searchKey + " contained in list: " +
                vnoList.contains(searchKey));                          // (15)

    Map<N, Integer> versionStatistics = new HashMap<N, Integer>();     // (16)
    for (int i = 0; i < versions.length; i++)                          // (17)
      versionStatistics.put(versions[i], downloads[i]);
    out.println("Map: " + versionStatistics);                          // (18)
    out.println("    Hash code for keys in the map:");
    for (N version : versions)                                         // (19)
      out.printf("    %10s: %s%n", version, version.hashCode());
    out.println("    Search key " + searchKey + " has hash code: " +
                searchKey.hashCode());                                 // (20)
    out.println("    Map contains search key " + searchKey + ": " +
                versionStatistics.containsKey(searchKey));             // (21)

    out.println("Sorted set:\n    " + (new TreeSet<N>(vnoList)));      // (22)
    out.println("Sorted map:\n    " +
                (new TreeMap<N, Integer>(versionStatistics)));         // (23)

    out.println("List before sorting: " + vnoList);
    Collections.sort(vnoList, null);                                   // (24)
    out.println("List after sorting:  " + vnoList);

    int resultIndex = Collections.binarySearch(vnoList, searchKey, null);// (25)
    out.println("Binary search in list found key " + searchKey +
                " at index: " + resultIndex);
  }
}