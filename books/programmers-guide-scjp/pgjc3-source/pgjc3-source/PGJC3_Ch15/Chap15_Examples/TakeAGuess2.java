import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class TakeAGuess2 {
  final static int NUM_DIGITS = 5;

  public static void main(String[] args) {

    // Sanity check on the given data.
    if (args.length != NUM_DIGITS) {
      System.err.println("Guess should be " + NUM_DIGITS + " digits.");
      return;
    }

    /* Initialize the solution list. This program has a fixed solution. */
    List<String> secretSolution = new ArrayList<String>();                // (1)
    secretSolution.add("5"); secretSolution.add("3");
    secretSolution.add("2"); secretSolution.add("7");
    secretSolution.add("2");

    // Convert the user's guess from string array to list.                   (2)
    List<String> guess = new ArrayList<String>();
    for (String argument : args)
      guess.add(argument);

    // Find the number of digits that were correctly included.               (3)
    List<String> duplicate = new ArrayList<String>(secretSolution);
    int numOfDigitsIncluded = 0;
    for (int i=0; i<NUM_DIGITS; i++)
      if (duplicate.remove(guess.get(i))) numOfDigitsIncluded++;

    /* Find the number of correctly placed digits by comparing the two
       lists, element by element, counting each correct placement. */
    // Need two iterators to traverse through the guess and solution lists.  (4)
    ListIterator<String> correct = secretSolution.listIterator();
    ListIterator<String> attempt = guess.listIterator();
    int numOfDigitsPlaced = 0;
    while (correct.hasNext())
      if (correct.next().equals(attempt.next())) numOfDigitsPlaced++;

    // Print the results.
    System.out.println(numOfDigitsIncluded + " digit(s) correctly included.");
    System.out.println(numOfDigitsPlaced +   " digit(s) correctly placed.");
  }
}