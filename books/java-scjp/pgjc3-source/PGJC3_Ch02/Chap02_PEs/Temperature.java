//Filename: Temperature.java
/* Identifiers and keywords in Java are case-sensitive. Therefore, the
   case of the file name must match the class name, the keywords must
   all be written in lowercase. The name of the String class has a
   capital S. The main method must be static and take an array of
   String objects as an argument. */
public class Temperature {
  public static void main(String[] args) {  // Correct method signature
    double fahrenheit = 62.5;
    // /* identifies the start of a "starred" comment.
    // */ identifies the end.
    /* Convert */
    double celsius = f2c(fahrenheit);
    // '' delimits character literals, "" delimits string literals.
    // Only first character literal is quoted as string to avoid addition.
    // The second char literal is implicitly converted to its string
    // representation, as string concatenation is performed by
    // the last + operator.
    // Java is case-sensitive. The name Celsius should be changed to
    // the variable name celsius.
    System.out.println(fahrenheit + "F" + " = " + celsius + 'C');
  }
  /* Method should be declared static. */
  static double f2c(double fahr) {  // Note parameter type should be double.
    return (fahr - 32) * 5 / 9;
  }
}