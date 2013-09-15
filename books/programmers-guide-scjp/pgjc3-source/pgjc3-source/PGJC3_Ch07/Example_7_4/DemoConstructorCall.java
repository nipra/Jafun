
class Light {

  // Fields:
  private int     noOfWatts;      // wattage
  private boolean indicator;      // on or off
  private String  location;       // placement

  // Constructors:
  Light() {                                  // (1) Explicit default constructor
    noOfWatts = 0;
    indicator = false;
    location  = "X";
    System.out.println("Returning from default constructor no. 1.");
  }
  Light(int watts, boolean onOffState) {                      // (2) Non-default
    noOfWatts = watts;
    indicator = onOffState;
    location  = "X";
    System.out.println("Returning from non-default constructor no. 2.");
  }
  Light(int noOfWatts, boolean indicator, String location) {  // (3) Non-default
    this.noOfWatts = noOfWatts;
    this.indicator = indicator;
    this.location  = location;
    System.out.println("Returning from non-default constructor no. 3.");
  }
}
//______________________________________________________________________________
public class DemoConstructorCall {
  public static void main(String[] args) {                    // (4)
    System.out.println("Creating Light object no. 1.");
    Light light1 = new Light();
    System.out.println("Creating Light object no. 2.");
    Light light2 = new Light(250, true);
    System.out.println("Creating Light object no. 3.");
    Light light3 = new Light(250, true, "attic");
  }
}
