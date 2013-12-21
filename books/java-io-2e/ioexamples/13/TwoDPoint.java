public class TwoDPoint {
  private double x;
  private double y;

  public TwoDPoint(double x, double y) {
    this.x = x;
    this.y = y;
  }

  public double getX() {
    return x;
  }

  public double getY() {
    return y;
  }
  
  public String toString() {
    return "[TwoDPoint:x=" + this.x + ", y=" + y +"]";
  }
}
