import java.util.Collections;
import java.util.NavigableSet;
import java.util.TreeSet;
import static java.lang.System.out;

public class SetNavigation {
  public static void main(String[] args) {

    NavigableSet<String> strSetA = new TreeSet<String>();                    // (1)
    Collections.addAll(strSetA, "Strictly", "Java", "dancing", "ballroom");  // (2)
    out.println("Before: " + strSetA);       // [Java, Strictly, ballroom, dancing]

    out.println("\nSubset-views:");                  // (3)
    out.println(strSetA.headSet("ballroom", true));  // [Java, Strictly, ballroom]
    out.println(strSetA.headSet("ballroom", false)); // [Java, Strictly]
    out.println(strSetA.tailSet("Strictly", true));  // [Strictly, ballroom,
                                                     //  dancing]
    out.println(strSetA.tailSet("Strictly", false)); // [ballroom, dancing]
    out.println(strSetA.subSet("A", false, "Z", false )); // [Java, Strictly]
    out.println(strSetA.subSet("a", false, "z", false )); // [ballroom, dancing]

    out.println("\nClosest-matches:");               // (4)
    out.println(strSetA.ceiling("ball"));            // ballroom
    out.println(strSetA.floor("ball"));              // Strictly
    out.println(strSetA.higher("ballroom"));         // dancing
    out.println(strSetA.lower("ballroom"));          // Strictly

    out.println("\nReverse order:");                 // (5)
    out.println(strSetA.descendingSet());    // [dancing, ballroom, Strictly, Java]

    out.println("\nFirst-last elements:");           // (6)
    out.println(strSetA.pollFirst());                // Java
    out.println(strSetA.pollLast());                 // dancing

    out.println("\nAfter: " + strSetA);              // [Strictly, ballroom]
  }
}