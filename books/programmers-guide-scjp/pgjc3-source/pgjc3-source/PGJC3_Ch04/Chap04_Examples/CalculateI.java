import static java.lang.Math.PI;           // (1) Static field
import static java.lang.Math.sqrt;         // (2) Static method
// Only specified static members are imported.

public class CalculateI {
  public static void main(String[] args) {
    double x = 3.0, y = 4.0;
    double squareroot = sqrt(y);           // Simple name of static method
    double hypotenuse = Math.hypot(x, y);  // (3) Requires type name.
    double area = PI * y * y;              // Simple name of static field
    System.out.printf("Square root: %.2f, hypotenuse: %.2f, area: %.2f%n",
                        squareroot, hypotenuse, area);
  }
}