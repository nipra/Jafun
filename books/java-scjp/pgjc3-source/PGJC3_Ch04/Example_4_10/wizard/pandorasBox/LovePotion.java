//File: LovePotion.java
package wizard.pandorasBox;                // (1) Package declaration

public class LovePotion {                  // (2) Accessible outside package
  String potionName;
  public LovePotion(String name) { potionName = name; }
  public String toString() { return potionName; }
}