import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
public class TraverseHashSetAndLinkedHashSet {
  public static void main(String[] args) {
    HashSet<String> set1 = new HashSet<String>();                         // (1)
    set1.add("breakfast"); set1.add("lunch"); set1.add("dinner");
    System.out.println("Serving meals from a HashSet (order can vary):");
    for (String meal : set1)
      System.out.println(meal);

    Set<String> set2 = new LinkedHashSet<String>();                       // (2)
    set2.add("breakfast"); set2.add("lunch"); set2.add("dinner");
    System.out.println("Serving meals from a LinkedHashSet" +
                       " (always insertion order):");
    for (String meal : set2)
      System.out.println(meal);
  }
}