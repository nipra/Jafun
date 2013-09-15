
public class Average7 {

  public static void main(String[] args) {
    try {                                                      // (1)
      printAverage(100, 0);                                    // (2)
    } catch (ArithmeticException ae) {                         // (3)
      ae.printStackTrace();                                    // (4)
      System.out.println("Exception handled in " +             // (5)
      "main().");
    } finally {
      System.out.println("Finally in main().");                // (6)
    }
    System.out.println("Exit main().");                        // (7)
  }

  public static void printAverage(int totalSum, int totalNumber) {
    try {                                                      // (8)
      int average = computeAverage(totalSum, totalNumber);     // (9)
      System.out.println("Average = " +                        // (10)
          totalSum + " / " + totalNumber + " = " + average);
    } catch (IllegalArgumentException iae) {                   // (11)
      iae.printStackTrace();                                   // (12)
      System.out.println("Exception handled in " +             // (13)
      "printAverage().");
    } finally {
      System.out.println("Finally in printAverage().");        // (14)
    }
    System.out.println("Exit printAverage().");                // (15)
  }

  public static int computeAverage(int sum, int number) {
    System.out.println("Computing average.");
    if (number == 0)                                           // (16)
      throw new ArithmeticException("Integer division by 0");  // (17)
    return sum/number;                                         // (18)
  }
}