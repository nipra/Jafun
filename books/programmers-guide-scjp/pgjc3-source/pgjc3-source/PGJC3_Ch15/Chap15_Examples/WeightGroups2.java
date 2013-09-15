import java.util.Arrays;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;

public class WeightGroups2 {
  public static void main(String[] args) {

    // Create a navigable map to store the frequency for each group.
    NavigableMap<Integer, Integer> groupFreqMap =
                                         new TreeMap<Integer, Integer>();   // (1)
    // Determine the frequencies:                                           // (2)
    for (String argument : args) {
      // Get the value from an argument and group into intervals of 5.
      double weight = Double.parseDouble(argument);
      int weightGroup = (int) Math.round(weight/5)*5;
      Integer frequency = groupFreqMap.get(weightGroup);
      // Increment frequency if necessary.
      frequency = (frequency == null) ? 1 : frequency+1;
      groupFreqMap.put(weightGroup, frequency);
    }

    // Print statistics about the frequency map:
    System.out.println("Group frequency map: " + groupFreqMap);             // (3)
    System.out.println("No. of weight groups: " + groupFreqMap.size());     // (4)

    System.out.println("First entry: " + groupFreqMap.firstEntry());        // (5)
    System.out.println("Last entry: " + groupFreqMap.lastEntry());          // (6)

    System.out.println("Greatest entry <= 77: " +
                        groupFreqMap.floorEntry(77));                       // (7)
    System.out.println("Smallest key > 90: " +
                        groupFreqMap.higherKey(90));                        // (8)

    System.out.println("Groups >= 75: " + groupFreqMap.tailMap(75, true));  // (9)
    System.out.println("Groups <  75: " + groupFreqMap.headMap(75, false)); // (10)

    // Print the histogram for the weight groups:
    System.out.println("Histogram:");
    int numRegistered = printHistogram(groupFreqMap);                       // (11)
    System.out.println("Number of weights registered: " + numRegistered);

    // Poll the navigable map:                                                 (12)
    System.out.println("Histogram (by polling):");
    int sumValues = 0;
    while (!groupFreqMap.isEmpty()) {
      Map.Entry<Integer, Integer> entry = groupFreqMap.pollFirstEntry();
      int frequency = entry.getValue();
      sumValues += frequency;
      System.out.printf("%5s: %s%n", entry.getKey(), frequency);
    }
    System.out.println("Number of weights registered: " + sumValues);
  }

  /** Prints histogram from a navigable map containing frequencies.
   *  Returns the sum of frequencies. */
  public static <K> int printHistogram(NavigableMap<K, Integer> freqMap) {  // (13)
    // Create a set of entries in ascending key order.
    Set<Map.Entry<K, Integer>> navEntrySet = freqMap.entrySet();            // (14)
    int sumValues= 0;
    // Traverse the set of entries to print the histogram:
    for (Map.Entry<K, Integer> entry : navEntrySet) {                       // (15)
      /* Extract frequency value from entry.
       * Use the Arrays.fill() method to fill a char array with equivalent
       * number of '*' as the frequency value.
       * Convert the char array to string in order to print. */
      int frequency = entry.getValue();
      sumValues += frequency;
      char[] bar = new char[frequency];
      Arrays.fill(bar, '*');
      // Print key and bar.
      System.out.printf("%5s: %s%n", entry.getKey(), new String(bar));
    }
    return sumValues;
  }
}