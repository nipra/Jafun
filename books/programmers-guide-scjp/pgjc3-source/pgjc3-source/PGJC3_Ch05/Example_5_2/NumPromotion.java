
public class NumPromotion {
  public static void main(String[] args) {
    byte   b = 32;
    char   c = 'z';                // Unicode value 122 (\u007a)
    short  s = 256;
    int    i = 10000;
    float  f = 3.5F;
    double d = 0.5;
    double v = (d * i) + (f * - b) - (c / s);     // (1) 4888.0D
    System.out.println("Value of v: " + v);
  }
}