
public class Digits {

  public static void main(String[] args) {
    System.out.println(digitToString('7') + " " + digitToString('8') + " " +
                       digitToString('6'));
  }

  public static String digitToString(char digit) {
    String str = "";
    switch(digit) {
      case '1': str = "one";   break;
      case '2': str = "two";   break;
      case '3': str = "three"; break;
      case '4': str = "four";  break;
      case '5': str = "five";  break;
      case '6': str = "six";   break;
      case '7': str = "seven"; break;
      case '8': str = "eight"; break;
      case '9': str = "nine";  break;
      case '0': str = "zero";  break;
      default:  System.out.println(digit + " is not a digit!");
    }
    return str;
  }
}