import static java.lang.System.out;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Pattern;

class MultiLineMode {
  public static void main(String[] args) throws IOException {
    String source = args[0];                   // (1) Filename from program args
    Pattern pattern = Pattern.compile("[a-zA-Z]+",
                                      Pattern.MULTILINE);// (2) Search pattern
    Scanner lexer = new Scanner(new File(source));// (3) Create a scanner
    // Outer loop:
    while (lexer.hasNextLine()) {                 // (4) Lookahead for next line
      String match = lexer.findInLine(pattern);   // (5) Find the first match
      // Inner loop:
      while (match != null) {                     // (6) Parse rest of the line
        out.println(match);                       // (7) Process the match
        match = lexer.findInLine(pattern);        // (8) Get the next match
      }
      lexer.nextLine();                           // (9) Clear rest of the line
    }
    IOException ioe = lexer.ioException();
    if (ioe != null)                             // (10) Check for read problem
      throw ioe;
    lexer.close();                                // (11) Close the scanner
  }
}