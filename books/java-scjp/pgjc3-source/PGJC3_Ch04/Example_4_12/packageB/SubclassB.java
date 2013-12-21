
//Filename: SubclassB.java                             (6)
package packageB;
import packageA.*;

public class SubclassB extends SuperclassA {
  void subclassMethodB() { superclassMethodA(); }   // (7) OK.
}

class AnyClassB {
  SuperclassA obj = new SuperclassA();
  void anyClassMethodB() {
    obj.superclassVarA = 20;                        // (8) OK.
  }
}