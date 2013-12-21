import static java.lang.System.out;

import java.util.Scanner;

class BareBonesTokenizer {
  public static void main(String[] args) {
    String input = "The world will end today -- not!";// (1) String as source
    Scanner lexer = new Scanner(input);               // (2) Create a scanner
    while (lexer.hasNext()) {                         // (3) Processing loop
      out.println(lexer.next());                      // (4) Parsing
    } 
    lexer.close();                                    // (5) Close the scanner
  }
}