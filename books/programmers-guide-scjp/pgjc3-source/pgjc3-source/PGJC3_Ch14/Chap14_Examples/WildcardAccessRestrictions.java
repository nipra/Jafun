class WildcardAccessRestrictions {

//static void checkIt(Node s0) {                    // (h1)
//static void checkIt(Node<?> s0) {                 // (h2)
//static void checkIt(Node<? extends Number> s0) {  // (h3)
//static void checkIt(Node<? super Number> s0) {    // (h4)
  static void checkIt(Node<Number> s0) {            // (h5)
    // Local variables
    Object  object  = new Object();                 // (v1)
    Number  number  = 1.5;                          // (v2)
    Integer integer = 10;                           // (v3)
    // Method calls
    s0.setData(object);                             // (s1)
    s0.setData(number);                             // (s2)
    s0.setData(integer);                            // (s3)
    object  = s0.getData();                         // (s4)
    number  = s0.getData();                         // (s5)
    integer = s0.getData();                         // (s6)
    s0.setData(s0.getData());                       // (s7)
  }
}