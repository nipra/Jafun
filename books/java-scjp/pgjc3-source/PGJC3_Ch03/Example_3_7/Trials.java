
public class Trials {
  public static void main(String[] args) {
    // Declare and construct the local arrays:
    double[] storeMinimum = new double[5];               // (1)
    double[] trialArray = new double[15];                // (2)
    for (int i = 0; i < storeMinimum.length; ++i) {      // (3)

      // Initialize the array.
      randomize(trialArray);

      // Find and store the minimum value.
      storeMinimum[i] = findMinimum(trialArray);
    }

    // Print the minimum values:                            (4)
    for (int i = 0; i < storeMinimum.length; ++i)
      System.out.printf("%.4f%n", storeMinimum[i]);
  }

  public static void randomize(double[] valArray) {      // (5)
    for (int i = 0; i < valArray.length; ++i)
      valArray[i] = Math.random() * 100.0;
  }

  public static double findMinimum(double[] valArray) {  // (6)
    // Assume the array has at least one element.
    double minValue = valArray[0];
    for (int i = 1; i < valArray.length; ++i)
      minValue = Math.min(minValue, valArray[i]);
    return minValue;
  }
}