
public class Light {
  // Static variable
  static int counter;      // Default value 0 when class is loaded.

  // Instance variables:
  int     noOfWatts = 100; // Explicitly set to 100.
  boolean indicator;       // Implicitly set to default value false.
  String  location;        // Implicitly set to default value null.

  public static void main(String[] args) {
    Light bulb = new Light();
    System.out.println("Static variable counter: "     + Light.counter);
    System.out.println("Instance variable noOfWatts: " + bulb.noOfWatts);
    System.out.println("Instance variable indicator: " + bulb.indicator);
    System.out.println("Instance variable location: "  + bulb.location);
    return;
  }
}