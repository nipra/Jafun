import java.nio.charset.*;
import java.util.*;

class CharsetLister {

  public static void main(String[] args) {
    Map charsets = Charset.availableCharsets();
    Iterator iterator = charsets.keySet().iterator();
    while (iterator.hasNext()) {
      System.out.println(iterator.next());
    }
  }
}
