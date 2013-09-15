import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class TraveralUsingForEachLoop {
  public static void main(String[] args) {

    // (1) Create a list of Integers.
    Collection<Integer> intList = new ArrayList<Integer>();
    int[] values = { 9, 11, -4, 1, 13, 99, 1, 0 };
    for (int i : values) {
      intList.add(i);
    }

    //  intList = Arrays.asList(9, 11, -4, 1, 13, 99, 1, 0);
    System.out.println("Before: " + intList);         // (2)
/*
    Iterator<Integer> interator = intList.iterator(); // (3) Get an iterator.
    while (interator.hasNext()) {                     // (4) Loop
      int value = interator.next();                   // (5) The next element
      if (value < 1 || value > 10)                    // (6) Remove the element if
        interator.remove();                           //     its value is not
                                                      //     between 1 and 10.
    }
    System.out.println("After:  " + intList);         // (7)
    */
//    /*
    for(int value : intList) {
      if (value < 1 || value > 10)
        intList.remove(value);
    }
    System.out.println("After:  " + intList);         // (7)
//    */
  }
}