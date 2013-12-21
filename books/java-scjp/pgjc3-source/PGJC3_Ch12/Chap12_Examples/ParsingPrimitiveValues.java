import static java.lang.System.out;

import java.util.Locale;
import java.util.Scanner;

public class ParsingPrimitiveValues {

  /** Types of tokens to parse */
  enum TokenType { INT, LONG, FLOAT, DOUBLE, BOOL, STR }

  public static void main(String[] args) {

    // Using default delimiters (i.e. whitespace).
    // Note Norwegian locale format for floating-point numbers.
    String input = "123 45,56 false 567 722 blahblah";
    String delimiters = "default";
    Locale locale = new Locale("no", "NO");
    parse(locale, input, delimiters,
          TokenType.INT, TokenType.DOUBLE, TokenType.BOOL,
          TokenType.INT, TokenType.LONG,   TokenType.STR);

    // Note the use of backslash to escape characters in regex.
    input = "2008 | 2 | true";
    delimiters = "\\s*\\|\\s*";
    parse(null, input, delimiters,
          TokenType.INT, TokenType.INT, TokenType.BOOL);

    // Another example of a regex to specify delimiters.
    input = "Always = true | 2 $ U";
    delimiters = "\\s*(\\||\\$|=)\\s*";
    parse(null, input, delimiters,
          TokenType.STR, TokenType.BOOL, TokenType.INT, TokenType.STR);
  }

  /**
   * Parses the input using the locale, the delimiters and
   * expected sequence of tokens.
   */
  public static void parse(Locale locale, String input, String delimiters,
                           TokenType... tokenTypes) { // (1) Vararg
    Scanner lexer = new Scanner(input);            // (2) Create a scanner.
    if (!delimiters.equalsIgnoreCase("default")) { // (3) Change delimiters?
      lexer.useDelimiter(delimiters);
    }
    if (locale != null) {                          // (4) Change locale?
      lexer.useLocale(locale);
    }
    out.println("Locale: " + lexer.locale());
    out.println("Delim:  " + delimiters);
    out.println("Input:  " + input);
    out.print("Tokens: ");

    // (5) Iterate through the tokens:
    for (TokenType tType : tokenTypes) {
      if (!lexer.hasNext()) break;        // (6) Handle premature end of input.
      switch(tType) {
        case INT:    out.print("<" + lexer.nextInt() + ">"); break;
        case LONG:   out.print("<" + lexer.nextLong() + ">"); break;
        case FLOAT:  out.print("<" + lexer.nextFloat() + ">"); break;
        case DOUBLE: out.print("<" + lexer.nextDouble() + ">"); break;
        case BOOL:   out.print("<" + lexer.nextBoolean() + ">"); break;
        case STR:    out.print("<" + lexer.next() + ">"); break;
        default:     assert false;
      }
    }
    System.out.println("\n");
    lexer.close();                         // (7) Close the scanner.
  }
}