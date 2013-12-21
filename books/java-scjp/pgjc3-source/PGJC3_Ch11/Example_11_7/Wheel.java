import java.io.Serializable;

//public class Wheel implements Serializable {                        // (1)
public class Wheel {                                              // (1a)
  private int wheelSize;

  Wheel(int ws) { wheelSize = ws; }

  public String toString() { return "wheel size: " + wheelSize; }
}