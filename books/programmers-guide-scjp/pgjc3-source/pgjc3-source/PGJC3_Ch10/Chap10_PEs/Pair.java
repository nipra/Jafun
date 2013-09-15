/**
 * Aggregate (non-generic) pairs of arbitrary objects.
 */
public final class Pair {
  private Object first, second;

  /** Construct a Pair object. */
  public Pair(Object one, Object two) {
    first = one;
    second = two;
  }

  /** Provides access to the first aggregated object. */
  public Object getFirst() { return first; }

  /** Provides access to the second aggregated object. */
  public Object getSecond() { return second; }

  /** @return true if the pair of objects are identical. */
  public boolean equals(Object other) {
    if (! (other instanceof Pair)) return false;
    Pair otherPair = (Pair) other;
    return first.equals(otherPair.getFirst()) &&
           second.equals(otherPair.getSecond());
  }

  /** @return a hash code for the aggregate pair. */
  public int hashCode() {
    // XORing the hash codes to create a hash code for the pair.
    return first.hashCode() ^ second.hashCode();
  }

  /** @return the textual representation of the aggregated objects. */
  public String toString() {
    return "[" + first + "," + second + "]";
  }
}