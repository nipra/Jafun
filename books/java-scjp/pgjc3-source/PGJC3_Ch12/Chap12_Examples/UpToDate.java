import java.util.Date;
public class UpToDate {

  public static void main(String[] args) {

    // Get the current date:
    Date currentDate = new Date();
    System.out.println("Date formatted: " + currentDate);   
    System.out.println("Date value in milliseconds: " + currentDate.getTime());

    // Create a Date object with a specific value of time measured
    // in milliseconds from the epoch:
    Date date1 = new Date(1200000000000L);
    
    // Change the date in the Date object:
    System.out.println("Date before adjustment: " + date1);
    date1.setTime(date1.getTime() + 1000000000L);  
    System.out.println("Date after adjustment: " + date1);
    
    // Compare two dates:
    String compareStatus = currentDate.after(date1) ? "after" : "before";
    System.out.println(currentDate + " is " + compareStatus + " " + date1);
    
    // Set a date before epoch:
    date1.setTime(-1200000000000L);  
    System.out.println("Date before epoch: " + date1);   
  }
}