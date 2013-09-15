class OuterA {                                   // (1)
  class InnerA { }                               // (2)
}
//_____________________________________________________________________________
class SubclassC extends OuterA.InnerA {          // (3) Extends NSMC at (2)

  // (4) Mandatory non-default constructor:
  SubclassC(OuterA outerRef) {
    outerRef.super();                            // (5) Explicit super() call
  }
}
//_____________________________________________________________________________
class OuterB extends OuterA {                    // (6) Extends class at (1)
  class InnerB extends OuterB.InnerA { }         // (7) Extends NSMC at (2)
}
//_____________________________________________________________________________
public class Extending {
  public static void main(String[] args) {

    // (8) Outer instance passed explicitly in constructor call:
    new SubclassC(new OuterA());

    // (9) No outer instance passed explicitly in constructor call to InnerB:
    new OuterB().new InnerB();
  }
}