import java.nio.charset.*;
import java.util.*;

class AliasLister {

  public static void main(String[] args) {
    Map charsets = Charset.availableCharsets();
    Iterator iterator = charsets.values().iterator();
    while (iterator.hasNext()) {
      Charset cs = (Charset) iterator.next();
      System.out.print(cs.displayName());      
      if (cs.isRegistered()) {
        System.out.print(" (registered): "); 
      }
      else {
         System.out.print(" (unregistered): "); 
      }
      System.out.print(cs.name() );      
      Iterator names = cs.aliases().iterator();
      while (names.hasNext()) {
        System.out.print(", ");
        System.out.print(names.next());
      }
      System.out.println();
    }
  }
}
