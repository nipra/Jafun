
public class Average5 {

  public static void main(String[] args) {
    printAverage(100, 0);                                 // (1)
    System.out.println("Exit main().");                   // (2)
  }

  public static void printAverage(int totalSum, int totalNumber) {
    try {                                                 // (3)
      int average = computeAverage(totalSum, totalNumber);// (4)
      System.out.println("Average = " +                   // (5)
          totalSum + " / " + totalNumber + " = " + average);
    } finally {                                           // (6)
      System.out.println("Finally done.");
    }
    System.out.println("Exit printAverage().");           // (7)
  }

  public static int computeAverage(int sum, int number) {
    System.out.println("Computing average.");             // (8)
    return sum/number;                                    // (9)
  }
}