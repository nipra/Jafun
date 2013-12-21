import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

/** Comparison in superclass */
abstract class ProgrammerCMP implements Comparable<ProgrammerCMP> {
  protected String name;
  protected int loc;      // Lines of code
  ProgrammerCMP(String name, int loc) {
    this.name = name; this.loc = loc;
  }
  public int compareTo(ProgrammerCMP that) {
    return this.loc < that.loc ? - 1 :
      this.loc == that.loc ? 0 : 1 ;
  }
}
class JProgrammerCMP extends ProgrammerCMP {
  JProgrammerCMP (int loc) { super("JProgrammerCMP", loc); }
}
class CProgrammerCMP extends ProgrammerCMP {
  CProgrammerCMP (int loc) { super("CProgrammerCMP", loc); }
}

/** Comparison in subclasses */
abstract class Programmer {
  protected String name;
  protected int loc;      // Lines of code
  Programmer(String name, int loc) { this.name = name; this.loc = loc; }
}
class JProgrammer extends Programmer implements Comparable<JProgrammer>{
  JProgrammer (int loc) { super("JProgrammer", loc); }
  public int compareTo(JProgrammer that) {
    return this.loc < that.loc ? - 1 : this.loc == that.loc ? 0 : 1 ;
  }
}
class CProgrammer extends Programmer implements Comparable<CProgrammer> {
  CProgrammer (int loc) { super("CProgrammer", loc); }
  public int compareTo(CProgrammer that) {
    return this.loc < that.loc ? - 1 : this.loc == that.loc ? 0 : 1 ;
  }
}

/** Tests generic methods */
public class WhoIsComparable {
  public static <T extends Comparable<T>> T max(T obj1, T obj2) {   // (1)
    return (obj1.compareTo(obj2) < 0) ? obj2 : obj1;
  }
  public static <T extends Comparable<? super T>> T                 // (2)
    superMax(T obj1, T obj2) {
    return (obj1.compareTo(obj2) < 0) ? obj2 : obj1;
  }
  public static <T> T
    max(T obj1, T obj2, Comparator<? super T> comp) {               // (3)
    return (comp.compare(obj1, obj2) < 0) ? obj2 : obj1;
  }

  public static void main(String[] args) {
    JProgrammerCMP jProgCMP = new JProgrammerCMP(1000);
    CProgrammerCMP cProgCMP = new CProgrammerCMP(50);
    JProgrammer jProg = new JProgrammer(1000);
    CProgrammer cProg = new CProgrammer(50);

    ProgrammerCMP  progCMP;
    Programmer  prog;

    /* Using <T extends Comparable<T>> T max(T, T) method */
    // Comparison in superclass
    jProgCMP = max(jProgCMP, jProgCMP);          // (4) Compile-time Error!
    progCMP = WhoIsComparable.<ProgrammerCMP>max(jProgCMP, jProgCMP); // (5)
    progCMP = max(jProgCMP, cProgCMP);
    // Comparison in subclasses
    jProg = max(jProg, jProg);
    jProg = WhoIsComparable.<JProgrammer>max(jProg, jProg);
    prog = max(jProg, cProg);                    // Expected error.

    /* Using <T extends Comparable<? super T>> T superMax(T, T) method */
    // Comparison in superclass
    jProgCMP = superMax(jProgCMP, jProgCMP);     // (6)
    progCMP = WhoIsComparable.<ProgrammerCMP>superMax(jProgCMP, jProgCMP);
    progCMP = superMax(jProgCMP, cProgCMP);
    // Comparison in subclasses
    jProg = superMax(jProg, jProg);
    jProg = WhoIsComparable.<JProgrammer>superMax(jProg, jProg);
    prog = superMax(jProg, cProg);               // Expected error.
  }
}