//Filename: OuterInstances.java
class TLClass {                                              // (1)  TLC
  private String id = "TLClass ";                            // (2)
  public TLClass(String objId) { id = id + objId; }          // (3)
  public void printId() {                                    // (4)
    System.out.println(id);
  }

  class InnerB {                                             // (5)  NSMC
    private String id = "InnerB ";                           // (6)
    public InnerB(String objId) { id = id + objId; }         // (7)
    public void printId() {                                  // (8)
      System.out.print(TLClass.this.id + " : ");             // (9)  Refers to (2)
      System.out.println(id);                                // (10) Refers to (6)
    }

    class InnerC {                                           // (11) NSMC
      private String id = "InnerC ";                         // (12)
      public InnerC(String objId) { id = id + objId; }       // (13)
      public void printId() {                                // (14)
        System.out.print(TLClass.this.id + " : ");           // (15) Refers to (2)
        System.out.print(InnerB.this.id + " : ");            // (16) Refers to (6)
        System.out.println(id);                              // (17) Refers to (12)
      }
      public void printIndividualIds() {                     // (18)
        TLClass.this.printId();                              // (19) Calls (4)
        InnerB.this.printId();                               // (20) Calls (8)
        printId();                                           // (21) Calls (14)
      }
    } // InnerC
  } // InnerB
} // TLClass
//_____________________________________________________________________________
public class OuterInstances {                                // (22)
  public static void main(String[] args) {                   // (23)
    TLClass a = new TLClass("a");                            // (24)
    TLClass.InnerB b = a.new InnerB("b");                    // (25)
    TLClass.InnerB.InnerC c1 = b.new InnerC("c1");           // (26)
    TLClass.InnerB.InnerC c2 = b.new InnerC("c2");           // (27)
    b.printId();                                             // (28)
    c1.printId();                                            // (29)
    c2.printId();                                            // (30)
    TLClass.InnerB bb = new TLClass("aa").new InnerB("bb");  // (31)
    TLClass.InnerB.InnerC cc = bb.new InnerC("cc");          // (32)
    bb.printId();                                            // (33)
    cc.printId();                                            // (34)
    TLClass.InnerB.InnerC ccc =
      new TLClass("aaa").new InnerB("bbb").new InnerC("ccc");// (35)
    ccc.printId();                                           // (36)
    System.out.println("------------");
    ccc.printIndividualIds();                                // (37)
  }
}