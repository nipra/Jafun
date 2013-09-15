
class Light {
  // Final static variable                  (1)
  final public static double KWH_PRICE = 3.25;

  int noOfWatts;

  // Final instance method                  (2)
  final public void setWatts(int watt) {
    noOfWatts = watt;
  }

  public void setKWH() {
    // KWH_PRICE = 4.10;                 // (3) Not OK. Cannot be changed.
  }
}
//______________________________________________________________________________
class TubeLight extends Light {
  // Final method in superclass cannot be overridden.
  // This method will not compile.
  /*
    public void setWatts(int watt) {     // (4) Attempt to override.
         noOfWatts = 2*watt;
    }
   */
}
//______________________________________________________________________________
public class Warehouse {
  public static void main(String[] args) {

    final Light tableLight = new Light();// (5) Final local variable.
    tableLight.noOfWatts = 100;          // (6) OK. Changing object state.
//  tableLight = new Light();            // (7) Not OK. Changing final reference.

    final Light streetLight;             // (8) Not initialized.
//  streetLight.noOfWatts = 2000;        // (9) Not OK.
  }
}