import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import static java.lang.System.out;

public class FormattingNumbers {
  public static void main(String[] args) {

    // Create an array of locales:
    Locale[] locales = {                              
        Locale.getDefault(),                                // Default: GB/UK
        new Locale("no", "NO"),                             // Norway
        Locale.JAPAN                                        // Japan
    };

    // Create an array of number formatters:
    NumberFormat[] numFormatters = new NumberFormat[] {
        NumberFormat.getNumberInstance(),                   // Default: GB/UK
        NumberFormat.getNumberInstance(locales[1]),         // Norway
        NumberFormat.getNumberInstance(locales[2])          // Japan
    };
    
    // Create an array of currency formatters:
    NumberFormat[] currFormatters = new NumberFormat[] {
        NumberFormat.getCurrencyInstance(),                 // Default: GB/UK
        NumberFormat.getCurrencyInstance(locales[1]),       // Norway
        NumberFormat.getCurrencyInstance(locales[2])        // Japan
    };
    
    // Number to format:
    double number = 9876.598;

    // Format a number by different number formatters:
    out.println("Formatting the number: " + number);
    runFormatters(number, numFormatters, locales);
    
    // Set the max decimal digits to 2 for number formatters:
    for (NumberFormat nf : numFormatters) {
      nf.setMaximumFractionDigits(2);
    }
    out.println("\nFormatting the number " + number + " (to 2 dec. places):");
    runFormatters(number, numFormatters, locales);
    
    // Format a currency amount by different currency formatters:
    out.println("\nFormatting the currency amount: " + number);
    runFormatters(number, currFormatters, locales);
    
    // Parsing a number:
    runParsers("9876.598", numFormatters, locales);
    runParsers("9876,598", numFormatters, locales);
    runParsers("9876@598", numFormatters, locales);
    runParsers("@9876598", numFormatters, locales);   // Input error

    // Parsing a currency amount:
    runParsers("£9876.598", currFormatters, locales);
    runParsers("kr 9876,598", currFormatters, locales);
    runParsers("JPY 98@76598", currFormatters, locales);
    runParsers("@9876598", currFormatters, locales);  // Input error
  }
  
  /** Runs the formatters on the value. */
  static void runFormatters(double value, NumberFormat[] formatters,    // (1)
                            Locale[] locales) {
    for(int i = 0; i < formatters.length; i++)
      out.printf("%-24s: %s%n", locales[i].getDisplayName(),
                                formatters[i].format(value));
  }
  
  /** Runs the parsers on the input string. */
  static void runParsers(String inputString, NumberFormat[] formatters, // (2)
                         Locale[] locales) {
    out.println("\nParsing: " + inputString);
    for(int i = 0; i < formatters.length; i++)
      try {
        out.printf("%-24s: %s%n", locales[i].getDisplayName(), 
                                  formatters[i].parse(inputString));
      } catch (ParseException pe) {
        out.println(pe);
      }
  }
}