
class Light { /* ... */ }

class TubeLight extends Light { /* ... */ }

public class Overload {
  boolean testIfOn(Light aLight)         { return true; }    // (1)
  boolean testIfOn(TubeLight aTubeLight) { return false; }   // (2)

  public static void main(String[] args) {

    TubeLight tubeLight = new TubeLight();
    Light     light     = new Light();

    Overload client = new Overload();
    System.out.println(client.testIfOn(tubeLight));// (3) ==> method at (2)
    System.out.println(client.testIfOn(light));    // (4) ==> method at (1)

  }
}