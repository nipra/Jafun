import static java.lang.System.out;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class UsingVersionNumberComparator {

  /** Comparator for reverse natural ordering of natural numbers. */
  public static Comparator<VersionNumber> reverseComparatorVNO() {  // (1)
    return new Comparator<VersionNumber>() {
      public int compare(VersionNumber vno1, VersionNumber vno2) {
        return vno2.compareTo(vno1);           // Comparing vno2 with vno1.
      }
    };
  }

  public static void main(String[] args) {
    VersionNumber[] versions = new VersionNumber[] {                // (2)
        new VersionNumber(3, 49, 1), new VersionNumber(8, 19, 81),
        new VersionNumber(2, 48, 28), new VersionNumber(10, 23, 78),
        new VersionNumber(9, 1, 1) };

    List<VersionNumber> vnList = Arrays.asList(versions);           // (3)
    out.println("List before sorting:\n    " + vnList);
    Collections.sort(vnList, reverseComparatorVNO());               // (4)
    out.println("List after sorting according to " +
                "reverse natural ordering:\n    " + vnList);
    VersionNumber searchKey = new VersionNumber(9, 1, 1);
    int resultIndex = Collections.binarySearch(vnList, searchKey,
                                          reverseComparatorVNO());  // (5)
    out.println("Binary search in list using reverse natural ordering" +
                " found key " + searchKey + " at index: " + resultIndex);
  }
}