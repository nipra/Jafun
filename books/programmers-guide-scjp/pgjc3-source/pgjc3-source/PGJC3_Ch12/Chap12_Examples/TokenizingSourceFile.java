import static java.lang.System.out;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

class TokenizingSourceFile {
  public static void main(String[] args) throws IOException {
    String source = "TokenizingSourceFile.java";   // (1) String as source

    Scanner lexer = new Scanner(new File(source)); // (2) Create a scanner
    while (lexer.hasNext()) {                      // (3) Processing loop
      out.println(lexer.next());                   // (4) Parsing
    }
    IOException ioe = lexer.ioException();
    if (ioe != null)
      throw ioe;
    lexer.close();                                 // (5) Close the scanner
  }
}