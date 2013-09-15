// Demonstrates forward references.
public class StaticForwardReferences {

  static {                // (1) Static initializer block
    sf1 = 10;             // (2) OK. Assignment to sf1 allowed
    //  sf1 = if1;        // (3) Not OK. Non-static field access in static context
    //  int a = 2 * sf1;  // (4) Not OK. Read operation before declaration
    int b = sf1 = 20;     // (5) OK. Assignment to sf1 allowed
    int c = StaticForwardReferences.sf1;// (6) OK. Not accessed by simple name
  }

  static int sf1 = sf2 = 30;  // (7) Static field. Assignment to sf2 allowed
  static int sf2;             // (8) Static field
  int if1 = 5;                // (9) Non-static field

  static {                    // (10) Static initializer block
    int d = 2 * sf1;          // (11) OK. Read operation after declaration
    int e = sf1 = 50;         // (12)
  }

  public static void main(String[] args) {
    System.out.println("sf1: " + StaticForwardReferences.sf1);
    System.out.println("sf2: " + StaticForwardReferences.sf2);
  }
}