
public class SimpleVNO {
  // Does not override equals() or hashCode().

  private int release;
  private int revision;
  private int patch;

  public SimpleVNO(int release, int revision, int patch) {
    this.release  = release;
    this.revision = revision;
    this.patch    = patch;
  }

  public String toString() {
    return "(" + release + "." + revision + "." + patch + ")";
  }
}