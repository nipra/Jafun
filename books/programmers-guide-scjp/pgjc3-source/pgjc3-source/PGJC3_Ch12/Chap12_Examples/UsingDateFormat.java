import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

class UsingDateFormat {
  public static void main(String[] args) {

    // Create some date/time formatters:
    DateFormat[] dateTimeFormatters = new DateFormat[] {
        DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL,
                                       Locale.US),
        DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG,
                                       Locale.US),
        DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM,
                                       Locale.US),
        DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT,
                                       Locale.US)
    };
    // Style names:
    String[] styles = { "FULL", "LONG", "MEDIUM", "SHORT" };

    // Format current date/time using different date formatters:
    Date date = new Date();
    int i = 0;
    for(DateFormat dtf : dateTimeFormatters)
      System.out.printf("%-6s: %s%n", styles[i++], dtf.format(date));
  }
}