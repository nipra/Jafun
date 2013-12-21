import java.text.*;
import java.util.*;

public class PercentTable {

  public static void main(String[] args) {
    NumberFormat percentFormat = NumberFormat.getPercentInstance(Locale.ENGLISH);
    for (double d = 0.0; d <= 1.0; d += 0.005) {
      System.out.println(percentFormat.format(d));
    }
  }
}
