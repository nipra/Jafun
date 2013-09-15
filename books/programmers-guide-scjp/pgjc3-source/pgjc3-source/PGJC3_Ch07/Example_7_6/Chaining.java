
class Light {
  // Fields:
  private int     noOfWatts;
  private boolean indicator;
  private String  location;

  // Constructors:
  Light() {                                  // (1) Explicit default constructor
    this(0, false);
    System.out.println(
    "Returning from default constructor no. 1 in class Light");
  }
  Light(int watt, boolean ind) {                              // (2) Non-default
    this(watt, ind, "X");
    System.out.println(
    "Returning from non-default constructor no. 2 in class Light");
  }
  Light(int noOfWatts, boolean indicator, String location) {  // (3) Non-default
    super();                                                  // (4)
    this.noOfWatts = noOfWatts;
    this.indicator = indicator;
    this.location  = location;
    System.out.println(
        "Returning from non-default constructor no. 3 in class Light");
  }
}
//______________________________________________________________________________
class TubeLight extends Light {
  // Instance variables:
  private int tubeLength;
  private int colorNo;

  // Constructors:
  TubeLight(int tubeLength, int colorNo) {                    // (5) Non-default
    this(tubeLength, colorNo, 100, true, "Unknown");
    System.out.println(
           "Returning from non-default constructor no. 1 in class TubeLight");
  }
  TubeLight(int tubeLength, int colorNo, int noOfWatts,
            boolean indicator, String location) {             // (6) Non-default
    super(noOfWatts, indicator, location);                    // (7)
    this.tubeLength = tubeLength;
    this.colorNo    = colorNo;
    System.out.println(
           "Returning from non-default constructor no. 2 in class TubeLight");
  }
}
//______________________________________________________________________________
public class Chaining {
  public static void main(String[] args) {
    System.out.println("Creating a TubeLight object.");
    TubeLight tubeLightRef = new TubeLight(20, 5);            // (8)
  }
}