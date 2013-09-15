import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

public class WeightGroups {
  public static void main(String[] args) {

    // Create a map to store the frequency for each group.
    Map<Integer, Integer> groupFreqMap = new HashMap<Integer, Integer>();   // (1)

    // Determine the frequencies:
    for (String argument : args) {                                          // (2)
      // Get the value from an argument and group into intervals of 5.
      double weight = Double.parseDouble(argument);
      int weightGroup = (int) Math.round(weight/5)*5;                       // (3)
      Integer frequency = groupFreqMap.get(weightGroup);                    // (4)
      // Increment frequency if necessary.
      frequency = (frequency == null) ? 1 : frequency+1;                    // (5)
      groupFreqMap.put(weightGroup, frequency);                             // (6)
    }

    // Print the histogram:
    // Create a sorted set of groups (keys)
    SortedSet<Integer> groupSet
                       = new TreeSet<Integer>(groupFreqMap.keySet());       // (7)
    // Traverse the keys, looking up the frequency from the map.
    for (int group : groupSet) {                                            // (8)
      int frequency = groupFreqMap.get(group);                              // (9)
      /* Use the Arrays.fill() method to fill a char array with equivalent
       * number of '*' as the frequency value.
       * Convert the char array to string in order to print. */
      char[] bar = new char[frequency];
      Arrays.fill(bar, '*');                                                // (10)
      System.out.println(group + ":\t" + new String(bar));
    }
  }
}