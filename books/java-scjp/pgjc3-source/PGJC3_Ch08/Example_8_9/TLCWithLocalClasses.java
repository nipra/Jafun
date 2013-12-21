class Base {
  protected int nsf1;
}

class TLCWithLocalClasses {    // Top level Class
  private double nsf1;         // Non-static field
  private int    nsf2;         // Non-static field
  private static int sf;       // Static field

  void nonStaticMethod(final int fp) { // Non-static Method
    final int flv  = 10;       // final local variable
    final int hlv  = 30;       // final (hidden) local variable
    int nflv = 20;             // non-final local variable

    class NonStaticLocal extends Base { // Non-static local class
    //static int f1;           // (1) Not OK. Static members not allowed.
      final static int f2 = 10;// (2) final static members allowed.
      int    f3  = fp;         // (3) final param from enclosing method.
      int    f4  = flv;        // (4) final local var from enclosing method.
    //double f5  = nflv;       // (5) Not OK. Only finals from enclosing method.
      double f6  = nsf1;       // (6) Inherited from superclass.
      double f6a = this.nsf1;  // (6a) Inherited from superclass.
      double f6b = super.nsf1; // (6b) Inherited from superclass.
      double f7  = TLCWithLocalClasses.this.nsf1;// (7) In enclosing object.
      int    f8  = nsf2;       // (8)  In enclosing object.
      int    f9  = sf;         // (9)  static from enclosing class.
      int    hlv;              // (10) Hides local variable.
    }
  }

  static void staticMethod(final int fp) { // Static Method
    final int flv  = 10;       // final local variable
    final int hlv  = 30;       // final (hidden) local variable
    int nflv = 20;             // non-final local variable

    class StaticLocal extends Base { // Static local class
    //static int f1;           // (11) Not OK. Static members not allowed.
      final static int f2 = 10;// (12) final static members allowed.
      int    f3  = fp;         // (13) final param from enclosing method.
      int    f4  = flv;        // (14) final local var from enclosing method.
    //double f5  = nflv;       // (15) Not OK. Only finals from enclosing method.
      double f6  = nsf1;       // (16) Inherited from superclass.
      double f6a = this.nsf1;  // (16a) Inherited from superclass.
      double f6b = super.nsf1; // (16b) Inherited from superclass.
    //double f7  = TLCWithLocalClasses.this.nsf1; //(17) No enclosing object.
    //int    f8  = nsf2;       // (18)  No enclosing object.
      int    f9  = sf;         // (19)  static from enclosing class.
      int    hlv;              // (20) Hides local variable.
    }
  }
}