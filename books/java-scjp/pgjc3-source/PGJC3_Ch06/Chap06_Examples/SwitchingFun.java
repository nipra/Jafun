
public class SwitchingFun {

  enum SPICE_DEGREE {                          // (1)
    MILD, MEDIUM, HOT, SUICIDE;
  }

  public static void main(String[] args) {
    SPICE_DEGREE spiceDegree = SPICE_DEGREE.HOT;
    switch (spiceDegree) {
      case HOT:                                // (2a) OK!
//    case SPICE_LEVEL.HOT:                    // (2b) COMPILE-TIME ERROR!
        System.out.println("Have fun!");
        break;
      case SUICIDE:
        System.out.println("Good luck!");
        break;
      default:
        System.out.println("Enjoy!");
    }
  }
}