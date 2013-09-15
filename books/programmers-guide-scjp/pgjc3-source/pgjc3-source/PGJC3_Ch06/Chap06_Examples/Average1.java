
public class Average1 {

  public static void main(String[] args) {
    printAverage(100, 20);                                // (1a)
    System.out.println("Exit main().");                   // (2)
  }

  public static void printAverage(int totalSum, int totalNumber) {
    int average = computeAverage(totalSum, totalNumber);  // (3)
    System.out.println("Average = " +                     // (4)
        totalSum + " / " + totalNumber + " = " + average);
    System.out.println("Exit printAverage().");           // (5)
  }

  public static int computeAverage(int sum, int number) {
    System.out.println("Computing average.");             // (6)
    return sum/number;                                    // (7)
  }
}