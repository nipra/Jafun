class NonStaticForwardReferences {

  {                        // (1) Instance initializer block
    nsf1 = 10;             // (2) OK. Assignment to nsf1 allowed
    nsf1 = sf1;            // (3) OK. Static field access in non-static context
    //  int a = 2 * nsf1;  // (4) Not OK. Read operation before declaration
    int b = nsf1 = 20;     // (5) OK. Assignment to nsf1 allowed
    int c = this.nsf1;     // (6) OK. Not accessed by simple name
  }

  int nsf1 = nsf2 = 30;    // (7) Non-static field. Assignment to nsf2 allowed
  int nsf2;                // (8) Non-static field
  static int sf1 = 5;      // (9) Static field

  {                        // (10) Instance initializer block
    int d = 2 * nsf1;      // (11) OK. Read operation after declaration
    int e = nsf1 = 50;     // (12)
  }

  public static void main(String[] args) {
    NonStaticForwardReferences objRef = new NonStaticForwardReferences();
    System.out.println("nsf1: " + objRef.nsf1);
    System.out.println("nsf2: " + objRef.nsf2);
  }
}