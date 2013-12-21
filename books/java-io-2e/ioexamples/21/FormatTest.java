import java.text.*;

public class FormatTest {

  public static void main(String[] args) {

    NumberFormat nf = NumberFormat.getInstance();
    for (double x = Math.PI; x < 100000; x *= 10) {
      String formattedNumber = nf.format(x);
      System.out.println(formattedNumber + "\t" + x);
    }
  }
}
