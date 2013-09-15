import java.io.Serializable;

public class Unicycle implements Serializable {                     // (2)
  private Wheel wheel;                                              // (3)
//transient private Wheel wheel;                                    // (3a)

  Unicycle (Wheel wheel) { this.wheel = wheel; }

  public String toString() { return "Unicycle with " + wheel; }
}