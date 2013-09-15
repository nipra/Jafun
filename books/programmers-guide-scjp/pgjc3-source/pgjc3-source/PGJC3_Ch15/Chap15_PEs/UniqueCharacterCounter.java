import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class UniqueCharacterCounter {
  /**
   * A cache, mapping strings to number of unique characters in them.
   */
  static Map<String, Integer> globalCache = new HashMap<String, Integer>();

  public static int countUniqueCharacters(String aString) {
    Integer cachedResult = globalCache.get(aString);
    if (cachedResult != null)
      return cachedResult;
    // Result was not in the cache, calculate it.
    int length = aString.length();
    Set<Character> distinct = new TreeSet<Character>();
    Set<Character> duplicates = new TreeSet<Character>();
    // Determine whether each distinct character in the string is duplicated:
    for (int i = 0; i < length; i++) {
      Character character = aString.charAt(i);
      if (duplicates.contains(character))
        continue;
      boolean isDistinct = distinct.add(character);
      if (!isDistinct)
        duplicates.add(character);
    }
    // Remove duplicates from the distinct set to obtain unique characters:
    distinct.removeAll(duplicates);
    int result = distinct.size();
    // Put result in cache before returning:
    globalCache.put(aString, result);
    return result;
  }

  /**
   * Demonstrate the cache for mapping strings to number of unique characters
   * in them.
   * Prints the result of applying the operation to each command line argument.
   */
  public static void main(String[] args) {
    int nArgs = args.length;
    for (int i = 0; i < nArgs; i++) {
      String argument = args[i];
      int result = countUniqueCharacters(argument);
      System.out.println(argument + ": " + result);
    }
  }
}