
class Light {
  // Fields:
  int     noOfWatts;      // wattage
  boolean indicator;      // on or off
  String  location;       // placement

  // Static variable
  static int counter;     // No. of Light objects created.         (1)

  // Explicit default constructor
  Light(int noOfWatts, boolean indicator, String location) {
    this.noOfWatts = noOfWatts;
    this.indicator = indicator;
    this.location  = location;
    ++counter;          // Increment counter.
  }

  // Static method
  public static void writeCount() {
    System.out.println("Number of lights: " + counter);         // (2)
    // Compile-time error. Field noOfWatts is not accessible:
    // System.out.println("Number of Watts: " + noOfWatts);     // (3)
  }
}
//______________________________________________________________________________
public class Warehouse {
  public static void main(String[] args) {                      // (4)

    Light.writeCount();                              // Invoked using class name
    Light light1 = new Light(100, true, "basement"); // Create an object
    System.out.println(
        "Value of counter: " + Light.counter         // Accessed via class name
    );
    Light light2 = new Light(200, false, "garage");  // Create another object
    light2.writeCount();                             // Invoked using reference
    Light light3 = new Light(300, true, "kitchen");  // Create another object
    System.out.println(
        "Value of counter: " + light3.counter        // Accessed via reference
    );
    final int i;
  }
}