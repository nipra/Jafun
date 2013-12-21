public class FloatingPointFormatExample {

  public static void main(String[] args) {
    System.out.printf("Decimal:               %f\n", Math.PI);
    System.out.printf("Scientific notation:   %e\n", Math.PI);
    System.out.printf("Scientific notation:   %E\n", Math.PI);
    System.out.printf("Decimal/Scientific:    %g\n", Math.PI);
    System.out.printf("Decimal/Scientific:    %G\n", Math.PI);
    System.out.printf("Lowercase Hexadecimal: %a\n", Math.PI);
    System.out.printf("Uppercase Hexadecimal: %A\n", Math.PI);
  }    
