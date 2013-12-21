
public class Speed {

  public static void main(String[] args) {
    Speed objRef = new Speed();
    double speed = objRef.calcSpeed(-12.0, 3.0);                 // (1a)
//     double speed = objRef.calcSpeed(12.0, -3.0);              // (1b)
    // double speed = objRef.calcSpeed(12.0, 2.0);               // (1c)
    // double speed = objRef.calcSpeed(12.0, 0.0);               // (1d)
    System.out.println("Speed (km/h): " + speed);
  }

  /** Requires distance >= 0.0 and time > 0.0 */
  private double calcSpeed(double distance, double time) {
    assert distance >= 0.0;                                      // (2)
    assert time > 0.0 : "Time is not a positive value: " + time; // (3)
    double speed = distance / time;
    assert speed >= 0.0;                                         // (4)
    return speed;
  }
}