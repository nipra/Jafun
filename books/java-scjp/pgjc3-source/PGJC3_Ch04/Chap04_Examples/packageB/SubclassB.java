
//Filename: SubclassB.java
package packageB;
import packageA.*;

public class SubclassB extends SuperclassA {     // In packageB.

  SuperclassA objRefA = new SuperclassA();       // (1)

  void subclassMethodB(SubclassB objRefB) {
    objRefB.superclassMethodA();                 // (2) OK.
    objRefB.superclassVarA = 5;                  // (3) OK.
    objRefA.superclassMethodA();                 // (4) Not OK.
    objRefA.superclassVarA = 10;                 // (5) Not OK.
  }
}