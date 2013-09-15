
class Light {
  // Fields:
  private int     noOfWatts;
  private boolean indicator;
  private String  location;

  // Constructors:
  Light() {                                 // (1) Explicit default constructor
    this(0, false);
    System.out.println("Returning from default constructor no. 1.");
  }
  Light(int watt, boolean ind) {                             // (2) Non-default
    this(watt, ind, "X");
    System.out.println("Returning from non-default constructor no. 2.");
  }
  Light(int noOfWatts, boolean indicator, String location) { // (3) Non-default
    this.noOfWatts = noOfWatts;
    this.indicator = indicator;
    this.location  = location;
    System.out.println("Returning from non-default constructor no. 3.");
  }
}
//______________________________________________________________________________
public class DemoThisCall {
  public static void main(String[] args) {                   // (4)
    System.out.println("Creating Light object no. 1.");
    Light light1 = new Light();                              // (5)
    System.out.println("Creating Light object no. 2.");
    Light light2 = new Light(250, true);                     // (6)
    System.out.println("Creating Light object no. 3.");
    Light light3 = new Light(250, true, "attic");            // (7)
  }
}
