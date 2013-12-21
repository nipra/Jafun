
public class Average4 {

  public static void main(String[] args) {
    printAverage(100, 20);                                // (1)
    System.out.println("Exit main().");                   // (2)
  }

  public static void printAverage(int totalSum, int totalNumber) {
    try {                                                 // (3)
      int average = computeAverage(totalSum, totalNumber);// (4)
      System.out.println("Average = " +                   // (5)
          totalSum + " / " + totalNumber + " = " + average);
    } catch (ArithmeticException ae) {                    // (6)
      ae.printStackTrace();                               // (7)
      System.out.println("Exception handled in " +
      "printAverage().");            // (8)
    } finally {                                           // (9)
      System.out.println("Finally done.");
    }
    System.out.println("Exit printAverage().");           // (10)
  }

  public static int computeAverage(int sum, int number) {
    System.out.println("Computing average.");             // (11)
    return sum/number;                                    // (12)
  }
}