import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MatchMaker {
  public static void main(String[] args) {
    // All examples from the subsection "Regular Expression Fundamentals".
    matchMaker("o",             "All good things come to those who wait");
    matchMaker("who",           "Interrogation with who, whose and whom.");
    matchMaker("[^aeiouAEIOU]", "I said I am.");
    matchMaker("[-A-Z]",        "I-love-REGEX.");
    matchMaker(".[Hh]o", 
               "Who is who? Whose is it? To whom it may concern. How are you?");
    matchMaker("\\d\\d-\\d\\d-\\d\\d", "01-03-49 786 09-09-09");
    matchMaker("\\?$",                 "Who is who? Who me? Who else?");
    matchMaker("([Jj][aA][vV][aA])|([Cc]\\+\\+)", 
               "JaVA jAvA C++ jAv c+++1 javan C+");
    matchMaker("a?",                      "banana");
    matchMaker("\\d\\d?-\\d\\d?-\\d\\d?", "01-3-49 23-12 9-09-09 01-01-2010");
    matchMaker("a*",                      "baananaa");
    matchMaker("(0|[1-9]\\d*)\\.\\d\\d",  ".50 1.50 0.50 10.50 00.50 1.555");
    matchMaker("a+",                      "baananaa");
    matchMaker("\\d+\\.\\d+",             ".50 1.50 0. 10.50 00.50 1.555");
    matchMaker("<.+>",    "My <>very<> <emphasis>greedy</emphasis> regex");
    matchMaker("<.+?>",   "My <>very<> <emphasis>reluctant</emphasis> regex");
    matchMaker("<[^>]+>", "My <>very<> <emphasis>powerful</emphasis> regex");
    // Some more regular expression examples.
    matchMaker("(^[a-z])|(\\?$)",      "who is who? Who me? Who else?");
    matchMaker("[\\\\-^$.?*+()|]",     "\\-^$.?*+()|");
    matchMaker("[-+]?[0-9]+",          "+123 -34 567 2.3435");
    matchMaker("[a-zA-Z][a-zA-Z0-9]+", "+a123 -X34 567 m2.3mm435");
    matchMaker("[^,]+",                "+a123, -X34, 567, m2,3mm435");
    matchMaker("\\\\",                 "book\\\\chapter\\section\\");
    matchMaker("[^\\\\]+",             "book\\\\chapter\\section\\");
  }

  public static void matchMaker(String regexStr, String target) {      // (1)
    System.out.print("Index:   ");
    for (int i = 0; i < target.length(); i++) {
      System.out.print(i%10);
    }
    System.out.println();
    System.out.println("Target:  " + target);
    System.out.println("Pattern: " + regexStr);
    System.out.print(  "Match:   ");
    Pattern pattern = Pattern.compile(regexStr);                       // (2)
    Matcher matcher = pattern.matcher(target);                         // (3)
    while(matcher.find()) {                                            // (4)
      int startCharIndex = matcher.start();                            // (5)
      int lastPlus1Index = matcher.end();                              // (6)
      int lastCharIndex = startCharIndex == lastPlus1Index ?
                          lastPlus1Index : lastPlus1Index-1;
      String matchedStr = matcher.group();                             // (7)
      System.out.print("(" + startCharIndex + "," + lastCharIndex + ":" + 
                             matchedStr + ")");
    }
    System.out.println();
  }
}