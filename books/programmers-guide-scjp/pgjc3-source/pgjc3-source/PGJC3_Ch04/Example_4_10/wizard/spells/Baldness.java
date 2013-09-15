//File: Baldness.java
package wizard.spells;                       // (1)Package declaration

import wizard.pandorasBox.*;                 // (2) Type import on demand
import wizard.pandorasBox.artifacts.*;       // (3) Import of subpackage

public class Baldness extends Ailment {      // (4) Simple name for Ailment
  wizard.pandorasBox.LovePotion tlcOne;      // (5) Fully qualified name
  LovePotion tlcTwo;                         // (6) Class in same package
  Baldness(String name) {
    super(name);
    tlcOne = new wizard.pandorasBox.         // (7) Fully qualified name
    LovePotion("romance");
    tlcTwo = new LovePotion();               // (8) Class in same package
  }
}

class LovePotion // implements Magic         // (9) Not accessible
{ public void levitate(){} }