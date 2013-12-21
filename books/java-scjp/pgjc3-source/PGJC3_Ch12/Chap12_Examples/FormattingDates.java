import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;
class FormattingDates {
  public static void main(String[] args) throws ParseException {
    // Locale to use:
    Locale localeNOR = new Locale("no", "NO");              //  (1) Norway

    // Create some date formatters:                                     (2)
    DateFormat[] dateFormatters = new DateFormat[] {               
        DateFormat.getDateInstance(DateFormat.SHORT, localeNOR),
        DateFormat.getDateInstance(DateFormat.MEDIUM,localeNOR),
        DateFormat.getDateInstance(DateFormat.LONG,  localeNOR),
        DateFormat.getDateInstance(DateFormat.FULL, localeNOR)
    };

    // Parsing the date:                                                (3)
    System.out.println("Parsing:");
    Date date = new Date();
    for(DateFormat df : dateFormatters)
      try {
        String strDate = df.format(date);                            // (4)
        Date parsedDate = df.parse(strDate);                         // (5)
        System.out.println(strDate + "|" + df.format(parsedDate));
      } catch (ParseException pe) {
        System.out.println(pe);
      }
      
    // Leniency:                                                        (6)
    System.out.println("Leniency:");
    System.out.println("32.01.08|" + dateFormatters[0].parse("32.01.08|"));
  }
}