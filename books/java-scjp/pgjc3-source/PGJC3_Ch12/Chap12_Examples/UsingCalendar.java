import java.util.Calendar;
import java.util.Date;
public class UsingCalendar {
  public static void main(String[] args) {

    // Get a calendar with current time and print its date:
    Calendar calendar = Calendar.getInstance();
    printDate("The date in the calendar: ", calendar);

    // Convert to Date:
    Date date1 = calendar.getTime();
    System.out.println("The date in the calendar: " + date1);

    // Set calendar according to a Date:
    Date date2 = new Date(1200000000000L);
    System.out.println("The date is " + date2);
    calendar.setTime(date2);
    printDate("The date in the calendar: ", calendar);

    // Set values in a calendar
    calendar.set(Calendar.DAY_OF_MONTH, 33);
    calendar.set(Calendar.MONTH, 13);
    calendar.set(Calendar.YEAR, 2010);
    printDate("After setting: ", calendar);
    
    // Adding to a calendar
    calendar.add(Calendar.MONTH, 13);
    printDate("After adding: ", calendar);

    // Rolling a calendar
    calendar.roll(Calendar.MONTH, 13);
    printDate("After rolling: ", calendar);
    
    // First day of the week.
    System.out.println((calendar.SUNDAY == calendar.getFirstDayOfWeek() ?
                        "Sunday is" : "Sunday is not" ) +
                       " the first day of the week.");
  }

  static private void printDate(String prompt, Calendar calendar) {
    System.out.print(prompt);
    System.out.printf("%4d/%02d/%02d%n", 
                      calendar.get(Calendar.YEAR),
                     (calendar.get(Calendar.MONTH) + 1),  // Adjust for month
                      calendar.get(Calendar.DAY_OF_MONTH));
  }
}