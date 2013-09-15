
public class Average3 {

  public static void main(String[] args) {
    try {                                                 // (1)
      printAverage(100, 0);                               // (2)
    } catch (ArithmeticException ae) {                    // (3)
      ae.printStackTrace();                               // (4)
      System.out.println("Exception handled in " +
      "main().");                    // (5)
    }
    System.out.println("Exit main().");                   // (6)
  }

  public static void printAverage(int totalSum, int totalNumber) {
    try {                                                 // (7)
      int average = computeAverage(totalSum, totalNumber);// (8)
      System.out.println("Average = " +                   // (9)
          totalSum + " / " + totalNumber + " = " + average);
    } catch (IllegalArgumentException iae) {              // (10)
      iae.printStackTrace();                              // (11)
      System.out.println("Exception handled in " +
      "printAverage().");            // (12)
    }
    System.out.println("Exit printAverage().");           // (13)
  }

  public static int computeAverage(int sum, int number) {
    System.out.println("Computing average.");             // (14)
    return sum/number;                                    // (15)
  }
}