import java.util.HashSet;
import java.util.Set;

public class CharacterSets {
  public static void main(String[] args) {
    int numArgs = args.length;

    // A set keeping track of all characters previously encountered.
    Set<Character> encountered = new HashSet<Character>();      // (1)

    // For each program argument in the command line ...
    for (String argument : args) {

      // Convert the current argument to a set of characters.
      Set<Character> characters = new HashSet<Character>();     // (2)
      int size = argument.length();
      // For each character in the argument...
      for (int j=0; j<size; j++)
        // add character to the characters set.
        characters.add(argument.charAt(j));                     // (3)

      // Determine whether a common subset exists.                 (4)
      Set<Character> commonSubset = new HashSet<Character>(encountered);
      commonSubset.retainAll(characters);
      boolean areDisjunct = commonSubset.size()==0;

      if (areDisjunct) {
        System.out.println(characters + " and " + encountered + " are disjunct.");
      } else {
        // Determine superset and subset relations.                (5)
        boolean isSubset = encountered.containsAll(characters);
        boolean isSuperset = characters.containsAll(encountered);
        if (isSubset && isSuperset)
          System.out.println(characters + " is equivalent to " + encountered);
        else if (isSubset)
          System.out.println(characters + " is a subset of " + encountered);
        else if (isSuperset)
          System.out.println(characters + " is a superset of " + encountered);
        else
          System.out.println(characters + " and " + encountered + " have " +
                             commonSubset + " in common.");
      }

      // Update the set of characters encountered so far.
      encountered.addAll(characters);                           // (6)
    }
  }
}