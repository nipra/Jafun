
public class Wheel {                                               // (1a)
  private int wheelSize;

  Wheel(int ws) { wheelSize = ws; }

  int getWheelSize() { return wheelSize; }

  public String toString() { return "wheel size: " + wheelSize; }
}