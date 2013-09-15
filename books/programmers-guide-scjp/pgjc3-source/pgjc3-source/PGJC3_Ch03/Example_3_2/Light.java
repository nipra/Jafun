
public class Light {
  // Fields:
  int     noOfWatts;      // wattage
  boolean indicator;      // on or off
  String  location;       // placement

  // Constructor
  public Light(int noOfWatts, boolean indicator, String site) {
    String location;

    this.noOfWatts = noOfWatts;   // (1) Assignment to field.
    indicator = indicator;        // (2) Assignment to parameter.
    location = site;              // (3) Assignment to local variable.
    this.superfluous();           // (4)
    superfluous();                // equivalent to call at (4)
  }

  public void superfluous() { System.out.println(this); }  // (5)

  public static void main(String[] args) {
    Light light = new Light(100, true, "loft");
    System.out.println("No. of watts: " + light.noOfWatts);
    System.out.println("Indicator: "    + light.indicator);
    System.out.println("Location: "     + light.location);
  }
}