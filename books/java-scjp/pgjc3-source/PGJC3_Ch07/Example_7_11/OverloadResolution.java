import static java.lang.System.out;

class OverloadResolution {

  public void action(String str) {                  // (1)
    String signature = "(String)";
    out.println(str + " => " + signature);
  }

  public void action(String str, int m) {           // (2)
    String signature = "(String, int)";
    out.println(str + " => " + signature);
  }

  public void action(String str, int m, int n) {    // (3)
    String signature = "(String, int, int)";
    out.println(str + " => " + signature);
  }

  public void action(String str, Integer... data) { // (4)
    String signature = "(String, Integer[])";
    out.println(str + " => " + signature);
  }

  public void action(String str, Number... data) {  // (5)
    String signature = "(String, Number[])";
    out.println(str + " => " + signature);
  }

  public void action(String str, Object... data) {  // (6)
    String signature = "(String, Object[])";
    out.println(str + " => " + signature);
  }

  public static void main(String[] args) {
    OverloadResolution ref = new OverloadResolution();
    ref.action("(String)");                                  // (8)  calls (1)
    ref.action("(String, int)",           10);               // (9)  calls (2)
    ref.action("(String, Integer)",       new Integer(10));  // (10) calls (2)
    ref.action("(String, int, byte)",     10, (byte)20);     // (11) calls (3)
    ref.action("(String, int, int)",      10,  20);          // (12) calls (3)
    ref.action("(String, int, long)",     10,  20L);         // (13) calls (5)
    ref.action("(String, int, int, int)", 10,  20,  30);     // (14) calls (4)
    ref.action("(String, int, double)",   10,  20.0);        // (15) calls (5)
    ref.action("(String, int, String)",   10,  "what?");     // (16) calls (6)
    ref.action("(String, boolean)",       false);            // (17) calls (6)
  }
}