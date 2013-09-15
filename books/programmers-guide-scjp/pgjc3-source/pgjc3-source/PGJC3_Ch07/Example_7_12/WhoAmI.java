class Light { /* ... */ }
class LightBulb extends Light { /* ... */ }
class SpotLightBulb extends LightBulb { /* ... */ }
class TubeLight extends Light { /* ... */ }
class NeonLight extends TubeLight { /* ... */ }

public class WhoAmI {
  public static void main(String[] args) {
    boolean result1, result2, result3, result4, result5;
    Light light1 = new LightBulb();                    // (1)
    //  String str = (String) light1;                  // (2) Compile-time error.
    //  result1 = light1 instanceof String;            // (3) Compile-time error.

    result2 = light1 instanceof TubeLight;             // (4) false. Peer class.
    //  TubeLight tubeLight1 = (TubeLight) light1;     // (5) ClassCastException.

    result3 = light1 instanceof SpotLightBulb;         // (6) false: Superclass
    //  SpotLightBulb spotRef = (SpotLightBulb) light1;// (7) ClassCastException

    light1 = new NeonLight();                          // (8)
    if (light1 instanceof TubeLight) {                 // (9) true
      TubeLight tubeLight2 = (TubeLight) light1;       // (10) OK
      // Can now use tubeLight2 to access an object of the class NeonLight,
      // but only those members that the object inherits or overrides
      // from the class TubeLight.
    }
  }
}