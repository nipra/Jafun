
public class Percolate {

  public static void main (String[] args) {
    int[] dataSeq = {6,4,8,2,1};    // Create and initialize an array.

    // Write array before percolation:
    printIntArray(dataSeq);

    // Percolate:
    for (int index = 1; index < dataSeq.length; ++index)
      if (dataSeq[index-1] > dataSeq[index])
        swap(dataSeq, index-1, index);                          // (1)

    // Write array after percolation:
    printIntArray(dataSeq);
  }

  public static void swap(int[] intArray, int i, int j) {       // (2)
    int tmp = intArray[i]; intArray[i] = intArray[j]; intArray[j] = tmp;
  }

  public static void swap(int v1, int v2) {                     // (3)
    int tmp = v1; v1 = v2; v2 = tmp;
  }

  public static void printIntArray(int[] array) {               // (4)
    for (int value : array)
      System.out.print(" " + value);
    System.out.println();
  }
}