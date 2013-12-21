import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MatchAndReplace {
  public static void main(String[] args) {
    
    // Match and replace loop:
    Pattern pattern = Pattern.compile("be");
    String input = "What will be will be.";
    System.out.println(input);
    Matcher matcher = pattern.matcher(input);
    StringBuffer strBuf = new StringBuffer();
    while (matcher.find()) {                               // (1)
        matcher.appendReplacement(strBuf, "happen");       // (2)
    }
    matcher.appendTail(strBuf);                            // (3)
    System.out.println(strBuf);
    
    // Match and replace all:
    matcher.reset();
    String result = matcher.replaceAll("happen");          // (4)
    System.out.println(result);
  }
}