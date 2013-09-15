
class Superclass {
  protected double x = 3.0e+8;
}
//_____________________________________________________________________________
class TopLevelClass {                       // (1) Top-level Class
  private double x = 3.14;

  class Inner extends Superclass {          // (2) Non-static member Class
    public void printHidden() {             // (3)

      // (4) x from superclass:
      System.out.println("this.x: " + this.x);

      // (5) x from enclosing context:
      System.out.println("TopLevelClass.this.x: " + TopLevelClass.this.x);
    }
  } // Inner
} // TopLevelClass
//_____________________________________________________________________________
public class HiddenAndInheritedAccess {
  public static void main(String[] args) {
    TopLevelClass.Inner ref = new TopLevelClass().new Inner();
    ref.printHidden();
  }
}