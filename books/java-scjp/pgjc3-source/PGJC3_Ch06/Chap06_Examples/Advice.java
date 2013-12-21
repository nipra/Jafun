
public class Advice {

  public final static int LITTLE_ADVICE  = 0;
  public final static int MORE_ADVICE    = 1;
  public final static int LOTS_OF_ADVICE = 2;

  public static void main(String[] args) {
    dispenseAdvice(LOTS_OF_ADVICE);
  }

  public static void dispenseAdvice(int howMuchAdvice) {
    switch(howMuchAdvice) {                        // (1)
      case LOTS_OF_ADVICE:
        System.out.println("See no evil.");        // (2)
      case MORE_ADVICE:
        System.out.println("Speak no evil.");      // (3)
      case LITTLE_ADVICE:
        System.out.println("Hear no evil.");       // (4)
        break;                                     // (5)
      default:
        System.out.println("No advice.");          // (6)
    }
  }
}