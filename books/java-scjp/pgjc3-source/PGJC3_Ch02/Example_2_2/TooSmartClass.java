
public class TooSmartClass {
  public static void main(String[] args) {
    int weight = 10, thePrice;                         // (1) Local variables

    if (weight <  10) thePrice = 1000;
    if (weight >  50) thePrice = 5000;
    if (weight >= 10) thePrice = weight*10;            // (2) Always executed.
    System.out.println("The price is: " + thePrice);   // (3)
  }
}