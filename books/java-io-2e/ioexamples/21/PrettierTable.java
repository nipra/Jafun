import java.text.*;

public class PrettierTable {

  public static void main(String[] args) {
  
    NumberFormat myFormat = NumberFormat.getNumberInstance();
    FieldPosition fp = new FieldPosition(NumberFormat.INTEGER_FIELD);
    myFormat.setMaximumIntegerDigits(3);
    myFormat.setMaximumFractionDigits(2);
    myFormat.setMinimumFractionDigits(2);
    
    System.out.println("Degrees  Radians  Grads");
    for (double degrees = 0.0; degrees < 360.0; degrees++) {
      String radianString = myFormat.format(Math.PI * degrees / 180.0, 
       new StringBuffer(), fp).toString();
      radianString = getSpaces(3 - fp.getEndIndex()) + radianString;
      String gradString = myFormat.format(400 * degrees / 360, 
       new StringBuffer(), fp).toString();
      gradString = getSpaces(3 - fp.getEndIndex()) + gradString;
      String degreeString = myFormat.format(
           degrees, new StringBuffer(), fp).toString();
      degreeString = getSpaces(3 - fp.getEndIndex()) + degreeString;
      System.out.println(degreeString + "  " + radianString + "  " + gradString);
    }
  }
    
  public static String getSpaces(int n) {
    StringBuffer sb = new StringBuffer(n);
    for (int i = 0; i < n; i++) sb.append(' ');
    return sb.toString();
  }
}
