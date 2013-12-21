
public class ReliableVNO {
  // Overrides both equals() and hashCode().

  private int release;
  private int revision;
  private int patch;

  public ReliableVNO(int release, int revision, int patch) {
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
    if (!(obj instanceof ReliableVNO))              // (3)
      return false;
    ReliableVNO vno = (ReliableVNO) obj;            // (4)
    return vno.patch    == this.patch    &&         // (5)
           vno.revision == this.revision &&
           vno.release  == this.release;
  }

  public int hashCode() {                           // (6)
    int hashValue = 11;
    hashValue = 31 * hashValue + release;
    hashValue = 31 * hashValue + revision;
    hashValue = 31 * hashValue + patch;
    return hashValue;
  }
}