//File: Ailment.java
package wizard.pandorasBox.artifacts;      // (1) Package declaration

public class Ailment {                     // (2) Accessible outside package
  String ailmentName;
  public Ailment(String name) { ailmentName = name; }
  public String toString() { return ailmentName; }
}