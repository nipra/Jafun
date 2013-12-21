import java.util.Date;

public class DateFormatExample {

  public static void main(String[] args) {
    Date now = new Date();
    System.out.printf("two digit hour on a 24-hour clock: %tH/%TH\n", now, now);
    System.out.printf("two digit hour on a 12-hour clock: %tI/%TI\n", now, now);
    System.out.printf("one-or-two digit hour on a 24-hour clock: %tk/%Tk\n", 
     now, now);
    System.out.printf("one-or-two digit hour on a 12-hour: %tl/%Tl\n", now, now);
    System.out.printf("two digit minutes ranging from 00 to 59: %tH/%TH\n", 
     now, now);
    System.out.printf("two digit seconds ranging from 00 to 60 : %tS/%TS\n", 
     now, now);
    System.out.printf("milliseconds: %tL/%TL\n", now, now);
    System.out.printf("nanoseconds: %tN/%TN\n", now, now);
    System.out.printf("Locale-specific morning/afternoon indicator: %tp/%Tp\n", 
     now, now);
    System.out.printf("RFC 822 numeric time zone indicator: %tz/%Tz\n", now, now);
    System.out.printf("Time zone abbreviation: %tZ/%TZ\n", now, now);
    System.out.printf("seconds since the epoch: %ts/%Ts\n", now, now);
    System.out.printf("milliseconds since the epoch: %TQ\n", now);
    System.out.printf("localized month name: %tB/%TB\n", now, now);
    System.out.printf("localized, abbreviated month: %tb/%Tb\n", now, now);
    System.out.printf("localized, abbreviated month: %th/%Th\n", now, now);
    System.out.printf("localized day name: %tA/%TA\n", now, now);
    System.out.printf("localized, abbreviated day: %ta/%Ta\n", now, now);
    System.out.printf("two-digit century: %tC/%TC\n", now, now);
    System.out.printf("four digit year: %tY/%TY\n", now, now);
    System.out.printf("two-digit year: %ty/%Ty\n", now, now);
    System.out.printf("three-digit day of the year: %tj/%Tj\n", now, now);
    System.out.printf("two-digit month: %tm/%Tm\n", now, now);
    System.out.printf("two-digit day of the month: %td/%Td\n", now, now);
    System.out.printf("a one-or-two-digit day of the month: %te/%Te\n", now, now);
    System.out.printf("hours and minutes on a 24-hour clock: %tR/%TR\n", now, now);
    System.out.printf("hours, minutes, and seconds on a 24-hour clock: %tT/%TT\n", 
     now, now);
    System.out.printf("hours, minutes, and seconds on a 12-hour clock: %tr/%Tr\n", 
     now, now);
    System.out.printf("month/day/year: %tD/%TD\n", now, now);
    System.out.printf("ISO 8601 standard date: %tF/%TF\n", now, now);
    System.out.printf("Unix date format: %tc/%Tc\n", now, now);
  }
}
