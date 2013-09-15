//Exceptions
class InvalidHoursException extends Exception {}
class NegativeHoursException extends InvalidHoursException {}
class ZeroHoursException extends InvalidHoursException {}

class Light {

  protected String billType  = "Small bill";       // (1)

  protected double getBill(int noOfHours)
  throws InvalidHoursException {                   // (2)
    if (noOfHours < 0)
      throw new NegativeHoursException();
    double smallAmount = 10.0, smallBill = smallAmount * noOfHours;
    System.out.println(billType + ": " + smallBill);
    return smallBill;
  }

  public static void printBillType() {             // (3)
    System.out.println("Small bill");
  }

  public void banner() {                           // (4)
    System.out.println("Let there be light!");
  }
}
//______________________________________________________________________________
class TubeLight extends Light {

  public static String billType = "Large bill"; // (5) Hiding static field at (1).

  @Override
  public double getBill(final int noOfHours)
         throws ZeroHoursException {     // (6) Overriding instance method at (2).
    if (noOfHours == 0)
      throw new ZeroHoursException();
    double largeAmount = 100.0, largeBill = largeAmount * noOfHours;
    System.out.println(billType + ": " + largeBill);
    return largeBill;
  }

  public static void printBillType() {   // (7) Hiding static method at (3).
    System.out.println(billType);
  }

  public double getBill() {              // (8) Overloading method at (6).
    System.out.println("No bill");
    return 0.0;
  }
}
//______________________________________________________________________________
class NeonLight extends TubeLight {
  // ...
  public void demonstrate() throws InvalidHoursException {     // (9)
    super.banner();                              // (10) Invokes method at (4)
    super.getBill(20);                           // (11) Invokes method at (6)
    super.getBill();                             // (12) Invokes method at (8)
    ((Light) this).getBill(20);                  // (13) Invokes method at (6)
    System.out.println(super.billType);          // (14) Accesses field at (5)
    System.out.println(((Light) this).billType); // (15) Accesses field at (1)
    super.printBillType();                       // (16) Invokes method at (7)
    ((Light) this).printBillType();              // (17) Invokes method at (3)
  }
}
//______________________________________________________________________________
public class Client {
  public static void main(String[] args)
  throws InvalidHoursException {
    NeonLight neonRef = new NeonLight();
    neonRef.demonstrate();
  }
}