
public final class VersionNumber implements Comparable<VersionNumber> {

  private final int release;
  private final int revision;
  private final int patch;

  public VersionNumber(int release, int revision, int patch) {
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
    if (!(obj instanceof VersionNumber))            // (3)
      return false;
    VersionNumber vno = (VersionNumber) obj;        // (4)
    return vno.patch    == this.patch    &&         // (5)
           vno.revision == this.revision &&
           vno.release  == this.release;
  }

  public int hashCode() {                           // (6)
    int hashValue = 11;
    hashValue = 31 * hashValue + this.release;
    hashValue = 31 * hashValue + this.revision;
    hashValue = 31 * hashValue + this.patch;
    return hashValue;
  }

  public int compareTo(VersionNumber vno) {         // (7)

    // Compare the release numbers.                    (8)
    if (this.release != vno.release)
      return new Integer(release).compareTo(vno.release);

    // Release numbers are equal,                      (9)
    // must compare revision numbers.
    if (this.revision != vno.revision)
      return new Integer(revision).compareTo(vno.revision);

    // Release and revision numbers are equal,         (10)
    // patch numbers determine the ordering.
    return new Integer(patch).compareTo(vno.patch);
  }
}