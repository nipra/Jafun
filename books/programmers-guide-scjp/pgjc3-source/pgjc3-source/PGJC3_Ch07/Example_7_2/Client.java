//Exceptions
class InvalidHoursException extends Exception {}
class NegativeHoursException extends InvalidHoursException {}
class ZeroHoursException extends InvalidHoursException {}

class Light {

  protected String billType = "Small bill";       // (1) Instance field

  protected double getBill(int noOfHours)
                   throws InvalidHoursException { // (2) Instance method
    if (noOfHours < 0)
      throw new NegativeHoursException();
    double smallAmount = 10.0, smallBill = smallAmount * noOfHours;
    System.out.println(billType + ": " + smallBill);
    return smallBill;
  }

  public Light makeInstance() {                   // (3) Instance method
    return new Light();
  }

  public static void printBillType() {            // (4) Static method
    System.out.println("Small bill");
  }
}
//______________________________________________________________________________
class TubeLight extends Light {

  public static String billType = "Large bill";  // (5) Hiding field at (1).

  @Override
  public double getBill(final int noOfHours)
         throws ZeroHoursException {   // (6) Overriding instance method at (2).
    if (noOfHours == 0)
      throw new ZeroHoursException();
    double largeAmount = 100.0, largeBill = largeAmount * noOfHours;
    System.out.println(billType + ": " + largeBill);
    return largeBill;
  }

  public double getBill() {            // (7) Overloading method at (6).
    System.out.println("No bill");
    return 0.0;
  }

  @Override
  public TubeLight makeInstance() {    // (8) Overriding instance method at (3).
    return new TubeLight();
  }

  public static void printBillType() { // (9) Hiding static method at (4).
    System.out.println(billType);
  }
}
//______________________________________________________________________________
public class Client {
  public static void main(String[] args) throws InvalidHoursException { // (10)

    TubeLight tubeLight = new TubeLight();    // (11)
    Light     light1    = tubeLight;          // (12) Aliases.
    Light     light2    = new Light();        // (13)

    System.out.println("Invoke overridden instance method:");
    tubeLight.getBill(5);                     // (14) Invokes method at (6).
    light1.getBill(5);                        // (15) Invokes method at (6).
    light2.getBill(5);                        // (16) Invokes method at (2).

    System.out.println(
           "Invoke overridden instance method with covariant return:");
    System.out.println(
           light2.makeInstance().getClass()); // (17) Invokes method at (3).
    System.out.println(
        tubeLight.makeInstance().getClass()); // (18) Invokes method at (8).

    System.out.println("Access hidden field:");
    System.out.println(tubeLight.billType);   // (19) Accesses field at (5).
    System.out.println(light1.billType);      // (20) Accesses field at (1).
    System.out.println(light2.billType);      // (21) Accesses field at (1).

    System.out.println("Invoke hidden static method:");
    tubeLight.printBillType();                // (22) Invokes method at (9).
    light1.printBillType();                   // (23) Invokes method at (4).
    light2.printBillType();                   // (24) Invokes method at (4).

    System.out.println("Invoke overloaded method:");
    tubeLight.getBill();                      // (25) Invokes method at (7).
  }
}