import static java.lang.System.out;

import java.util.Scanner;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

public class Tokenizer {
  public static void main(String[] args) {
    tokenize("([Jj][aA][vV][aA])|([Cc]\\+\\+)", 
             "JaVA jAvA C++ jAv c+++1 javan C+", "default");
    tokenize("[a-z[A-Z]]+", "C:\\Program Files\\3MM\\MSN2Lite\\Help", "\\\\");
  }

  public static void tokenize(String regexStr, String source,
                              String delimiters) {               // (1)
    System.out.print("Index:   ");
    for (int i = 0; i < source.length(); i++) {
      System.out.print(i%10);
    }
    System.out.println();
    System.out.println("Target:  " + source);
    System.out.println("Delimit: " + delimiters);
    System.out.println("Pattern: " + regexStr);
    System.out.print(  "Match:   ");
    Pattern pattern = Pattern.compile(regexStr);                 // (2)
    Scanner lexer = new Scanner(source);                         // (3)
    if (!delimiters.equalsIgnoreCase("default"))
      lexer.useDelimiter(delimiters);                            // (5)
    while(lexer.hasNext()) {                                     // (4)
      if (lexer.hasNext(pattern)) {                              // (5)
        String matchedStr = lexer.next(pattern);                 // (5)
        MatchResult matchResult = lexer.match();                 // (6)
        int startCharIndex = matchResult.start();
        int lastPlus1Index = matchResult.end();
        int lastCharIndex = startCharIndex == lastPlus1Index ?
            lastPlus1Index : lastPlus1Index-1;
        out.print("(" + startCharIndex + "," + lastCharIndex + ":" + 
            matchedStr + ")");
      } else {
        lexer.next();                                            // (7)
      }
    }
    System.out.println();
  }
}