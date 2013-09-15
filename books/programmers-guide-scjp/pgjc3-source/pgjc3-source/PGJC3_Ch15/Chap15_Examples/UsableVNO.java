
public class UsableVNO {
  // Overrides equals(), but not hashCode().

  private int release;
  private int revision;
  private int patch;

  public UsableVNO(int release, int revision, int patch) {
    this.release  = release;
    this.revision = revision;
    this.patch    = patch;
  }

  public String toString() {
    return "(" + release + "." + revision + "." + patch + ")";
  }

  public boolean equals(Object obj) {               // (1)
    if (obj == this)                                // (2)
      return true;
    if (!(obj instanceof UsableVNO))                // (3)
      return false;
    UsableVNO vno = (UsableVNO) obj;                // (4)
    return vno.patch    == this.patch    &&         // (5)
           vno.revision == this.revision &&
           vno.release  == this.release;
  }
}