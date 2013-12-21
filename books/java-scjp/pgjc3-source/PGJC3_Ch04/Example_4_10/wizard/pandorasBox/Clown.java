//File: Clown.java
package wizard.pandorasBox;                      // (1) Package declaration

import wizard.pandorasBox.artifacts.Ailment;     // (2) Importing class

public class Clown implements Magic {
  LovePotion tlc;                                // (3) Class in same package
  wizard.pandorasBox.artifacts.Ailment problem;  // (4) Fully qualified class name
  Clown() {
    tlc = new LovePotion("passion");
    problem = new Ailment("flu");                // (5) Simple class name
  }
  public void levitate()  { System.out.println("Levitating"); }
  public void mixPotion() { System.out.println("Mixing " + tlc); }
  public void healAilment() { System.out.println("Healing " + problem); }

  public static void main(String[] args) {       // (6)
    Clown joker = new Clown();
    joker.levitate();
    joker.mixPotion();
    joker.healAilment();
  }
}

interface Magic { void levitate(); }             // (7)