import java.util.regex.Pattern;

public class Splitting {
  public static void main(String[] args) {
    
    System.out.println("===Using the Pattern.split() method===");
    doPatSplits("tom|dick|harry", "\\|", -1, 3);
    doPatSplits("|tom||dick|harry|", "\\|", -1, 6);

    System.out.println("===Using the String.split() method===");
    doStrSplits("tom|dick|harry", "\\|", -1, 3);
  }   
  
  public static void doPatSplits(String input, String splitRegex,
                                 int lowerLimit, int upperLimit) {     // (1)
    System.out.print("Input: " + input);
    System.out.println("    Split: " + splitRegex);
    System.out.println("Limit Length Results");
    Pattern splitPattern = Pattern.compile(splitRegex);                // (2)
    for (int limit = upperLimit; limit >= lowerLimit; limit--) {
      String[] results = splitPattern.split(input, limit);             // (3)
      System.out.printf("%3d%6d    ", limit, results.length);
      printCharSeqArray(results);
    }
  }
  
  public static void doStrSplits(String input, String splitRegex,       
                                 int lowerLimit, int upperLimit) {     // (4)
    System.out.print("Input: " + input);
    System.out.println("    Split: " + splitRegex);
    System.out.println("Limit Length Results");
    for (int limit = upperLimit; limit >= lowerLimit; limit--) {
      String[] results = input.split(splitRegex, limit);               // (5)
      System.out.printf("%3d%6d    ", limit, results.length);
      printCharSeqArray(results);
    }
  }
  
  static void printCharSeqArray(CharSequence[] array) {                // (6)
    System.out.print("{ ");
    for (int i = 0; i < array.length; i++) {
      System.out.print("\"" + array[i] + "\""); 
      System.out.print((i != array.length -1) ? ", " : " ");
    }
    System.out.println("}");
  }
}