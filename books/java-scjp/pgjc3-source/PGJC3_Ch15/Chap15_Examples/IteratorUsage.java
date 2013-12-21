import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class IteratorUsage {
  public static void main(String[] args) {

    // (1) Create a list of Integers.
    Collection<Integer> intList = new ArrayList<Integer>();
    int[] values = { 9, 11, -4, 1, 13, 99, 1, 0 };
    for (int i : values) {
      intList.add(i);
    }
    System.out.println("Before: " + intList);         // (2)

    Iterator<Integer> iterator = intList.iterator();  // (3) Get an iterator.
    while (iterator.hasNext()) {                      // (4) Loop
      int value = iterator.next();                    // (5) The next element
      if (value < 1 || value > 10)                    // (6) Remove the element if
        iterator.remove();                            //     its value is not
                                                      //     between 1 and 10.
    }
    System.out.println("After:  " + intList);         // (7)
  }
}