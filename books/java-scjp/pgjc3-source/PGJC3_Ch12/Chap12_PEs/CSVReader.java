import static java.lang.System.out;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Pattern;

public class CSVReader {
  public static void main(String[] args) throws IOException {
    FileWriter fw = new FileWriter("csv.txt");
    fw.write("2.5,25,250\n");
    fw.write("Hi,Hello,Howdy\n");
    fw.write("2008,2009,2010\n");
    fw.write("one,two,three\n");
    fw.close();
    BufferedReader source = new BufferedReader(new FileReader("csv.txt"));
    readCSV(source, 3);
    source.close();
  }
  /**
   * Reads values in CSV format.
   * @param source
   * @param numOfFields
   * @throws IOException
   */
  public static void readCSV(Readable source,
                             int numOfFields)throws IOException {
    Scanner lexer = new Scanner(source);
    Pattern csvPattern = compileCSVPattern(numOfFields);
    out.println("Pattern: " + csvPattern.pattern());
    Pattern splitPattern = Pattern.compile(",");
    while (lexer.hasNextLine()) {
      // Match fields on the line
      String record = lexer.findInLine(csvPattern);
      if (record != null) {
        // Split the record on the split pattern:
        String[] fields = splitPattern.split(record, numOfFields);
        out.println(Arrays.toString(fields));
      }
      lexer.nextLine();          // Clear line separator to continue.
    }
    IOException ioe = lexer.ioException();
    if (ioe != null)
      throw ioe;
  }

  /**
   * Creates a multiline-mode pattern that corresponds to the number of fields
   * specified in CSV format on each line/record:
   * ([^,]+),...,([^,]+)
   * Alternative regular expressions for CSV:
   * ^([^,]+),...,([^,]+)
   * ([^,]+),...,([^,]+)$
   * ^([^,]+),...,([^,]+)$
   * (.+),...,(.+)
   *
   * @param numOfFields
   * @return Pattern to match all the field values.
   */
  public static Pattern compileCSVPattern(int numOfFields) {
    assert numOfFields >= 1;
    String fieldPattern = "([^,]+)";
    String patternStr = fieldPattern;
    for (int i = 2; i <= numOfFields; i++) {
      patternStr += "," + fieldPattern;
    }
    return Pattern.compile(patternStr, Pattern.MULTILINE);
  }
}