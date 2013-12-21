class LabeledSkip {
  public static void main(String[] args) {
    int[][] squareMatrix = {{4, 3, 5}, {2, 1, 6}, {9, 7, 8}};
    int sum = 0;
    outer:                                                  // label
      for (int i = 0; i < squareMatrix.length; ++i){        // (1)
        for (int j = 0; j < squareMatrix[i].length; ++j) {  // (2)
          if (j == i) continue;                   // (3) Control to (5).
          System.out.println("Element[" + i + ", " + j + "]: " +
              squareMatrix[i][j]);
          sum += squareMatrix[i][j];
          if (sum > 10) continue outer;           // (4) Control to (6).
          // (5) Continue with inner loop.
        } // end inner loop
        // (6) Continue with outer loop.
      } // end outer loop
    System.out.println("sum: " + sum);
  }
}
