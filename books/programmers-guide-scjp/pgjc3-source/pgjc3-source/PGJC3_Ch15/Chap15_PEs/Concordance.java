import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Concordance {

  /** Map for the concordance. */
  public Map<Character, List<Integer>> indexMap =
      new HashMap<Character, List<Integer>>();

  /** Add each character and its index to the concordance */
  public Concordance(StringBuilder input) {
    for (int i = 0; i < input.length(); ++i) {
      addEntry(input.charAt(i), i);
    }
  }

  /** Update the list of indices for a given character */
  void addEntry(char key, int pos) {
    List<Integer> hits = indexMap.get(key);
    if (hits == null) {
      hits = new ArrayList<Integer>();
      indexMap.put(key, hits);
    }
    hits.add(pos);
  }

  public static void main(String[] args) {
    StringBuilder input = new StringBuilder();
    for (int i = 0; i < args.length; ++i)
      input.append(args[i]);
    Concordance conc = new Concordance(input);
    System.out.println(conc.indexMap);
  }
}