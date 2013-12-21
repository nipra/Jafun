class BreakOut {

  public static void main(String[] args) {

    for (int i = 1; i <= 5; ++i) {
      if (i == 4)
        break; // (1) Terminate loop. Control to (2).
      // Rest of loop body skipped when i gets the value 4.
      System.out.printf("%d    %.2f%n", i, Math.sqrt(i));
    } // end for
    // (2) Continue here.

    int n = 2;
    switch (n) {
      case 1:
        System.out.println(n);
        break;
      case 2:
        System.out.println("Inner for loop: ");
        for (int j = 0; j <= n; j++)
          if (j == 2)
            break; // (3) Terminate loop. Control to (4).
          else
            System.out.println(j);
      default:
        System.out.println("default: " + n); // (4) Continue here.
    }
  }
}