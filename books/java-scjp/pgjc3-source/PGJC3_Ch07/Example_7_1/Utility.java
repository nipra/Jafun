
class Light {                           // (1)
  // Instance fields:
            int     noOfWatts;          // wattage
  private   boolean indicator;          // on or off
  protected String  location;           // placement

  // Static field:
  private static int counter;           // no. of Light objects created

  // Constructor:
  Light() {
    noOfWatts = 50;
    indicator = true;
    location  = "X";
    counter++;
  }

  // Instance methods:
  public  void    switchOn()  { indicator = true; }
  public  void    switchOff() { indicator = false; }
  public  boolean isOn()      { return indicator; }
  private void    printLocation() {
    System.out.println("Location: " + location);
  }

  // Static methods:
  public static void writeCount() {
    System.out.println("Number of lights: " + counter);
  }
  //...
}
//______________________________________________________________________________
class TubeLight extends Light {         // (2) Subclass uses the extends clause.
  // Instance fields:
  private int tubeLength = 54;
  private int colorNo    = 10;

  // Instance methods:
  public int getTubeLength() { return tubeLength; }

  public void printInfo() {
    System.out.println("Tube length: "  + getTubeLength());
    System.out.println("Color number: " + colorNo);
    System.out.println("Wattage: "      + noOfWatts);     // Inherited.
//  System.out.println("Indicator: "    + indicator);     // Not Inherited.
    System.out.println("Indicator: "    + isOn());        // Inherited.
    System.out.println("Location: "     + location);      // Inherited.
//  printLocation();                                      // Not Inherited.
//  System.out.println("Counter: "   + counter);          // Not Inherited.
    writeCount();                                         // Inherited.
  }
  // ...
}
//______________________________________________________________________________
public class Utility {                  // (3)
  public static void main(String[] args) {
    new TubeLight().printInfo();
  }
}