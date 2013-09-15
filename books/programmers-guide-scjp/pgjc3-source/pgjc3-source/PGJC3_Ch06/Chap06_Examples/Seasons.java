
public class Seasons {

  public static void main(String[] args) {
    int monthNumber = 11;
    switch(monthNumber) {                                     // (1) Outer
      case 12: case 1: case 2:
        System.out.println("Snow in the winter.");
        break;
      case 3: case 4: case 5:
        System.out.println("Green grass in the spring.");
        break;
      case 6: case 7: case 8:
        System.out.println("Sunshine in the summer.");
        break;
      case 9: case 10: case 11:                               // (2)
        switch(monthNumber) { // Nested switch                   (3) Inner
          case 10:
            System.out.println("Halloween.");
            break;
          case 11:
            System.out.println("Thanksgiving.");
            break;
        } // end nested switch
        // Always printed for case labels 9, 10, 11
        System.out.println("Yellow leaves in the fall.");     // (4)
        break;
      default:
        System.out.println(monthNumber + " is not a valid month.");
    }
  }
}