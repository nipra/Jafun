import java.util.ArrayList;
import java.util.Collection;

public class IterateOverCollection {
  public static void main(String[] args) {

    // Create an empty collection of StringBuilders.
    Collection<StringBuilder> words = new ArrayList<StringBuilder>();   // (1)

    // An array of StringBuilders
    StringBuilder[] strArray = {
        new StringBuilder("t'noD"), new StringBuilder("etareti"),
        new StringBuilder("!em")
    };
    // Add StringBuilders from the array to the collection
    for (StringBuilder str : strArray) {                                // (2)
      words.add(str);
    }
    System.out.println("Before: " + words);
    //  Iterate over a collection of StringBuilders.
    //  Expression type is Collection<StringBuilder>,
    //  and element type is StringBuilder.
    for (StringBuilder word : words) {                                  // (3)
      System.out.print(word.reverse() + " ");
    }
    System.out.println();
    System.out.println("After: " + words);
  }
}