// Filename: Meal.java
public enum Meal {
  BREAKFAST(7,30), LUNCH(12,15), DINNER(19,45);             // (1)

  // Non-default constructor                                   (2)
  Meal(int hh, int mm) {
    assert (hh >= 0 && hh <= 23): "Illegal hour.";
    assert (mm >= 0 && mm <= 59): "Illegal mins.";
    this.hh = hh;
    this.mm = mm;
  }

  // Fields for the meal time:                                 (3)
  private int hh;
  private int mm;

  // Instance methods:                                         (4)
  public int getHour() { return this.hh; }
  public int getMins() { return this.mm; }
}