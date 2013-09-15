
public class Average6 {

  public static void main(String[] args) {
    System.out.println("Average: " + printAverage(100, 20));  // (1)
    System.out.println("Exit main().");                       // (2)
  }

  public static int printAverage(int totalSum, int totalNumber) {
    int average = 0;
    try {                                                     // (3)
      average = computeAverage(totalSum, totalNumber);        // (4)
      System.out.println("Average = " +                       // (5)
          totalSum + " / " + totalNumber + " = " + average);
      return average;                                         // (6)
    } finally {                                               // (7)
      System.out.println("Finally done.");
      return average*2;                                       // (8)
    }
  }

  public static int computeAverage(int sum, int number) {
    System.out.println("Computing average.");                 // (9)
    return sum/number;                                        // (10)
  }
}