
public class Average8 {
  public static void main(String[] args) {
    try {                                                    // (1)
      printAverage(100, 0);                                  // (2)
    } catch (IntegerDivisionByZero idbze) {                  // (3)
      idbze.printStackTrace();
      System.out.println("Exception handled in " +
      "main().");
    } finally {                                              // (4)
      System.out.println("Finally done in main().");
    }

    System.out.println("Exit main().");                      // (5)
  }

  public static void printAverage(int totalSum, int totalNumber)
  throws IntegerDivisionByZero {                             // (6)

    int average = computeAverage(totalSum, totalNumber);
    System.out.println("Average = " +
        totalSum + " / " + totalNumber + " = " + average);
    System.out.println("Exit printAverage().");              // (7)
  }

  public static int computeAverage(int sum, int number)
  throws IntegerDivisionByZero {                             // (8)

    System.out.println("Computing average.");
    if (number == 0)                                         // (9)
      throw new IntegerDivisionByZero("Integer Division By Zero");
    return sum/number;                                       // (10)
  }
}

class IntegerDivisionByZero extends Exception {              // (11)
  IntegerDivisionByZero(String str) { super(str); }          // (12)
}