/**
 * Aggregate generic pairs of arbitrary objects.
 */
public final class GenericPair<F, S> {
  private F first;
  private S second;

  /** Construct a Pair object. */
  public GenericPair(F one, S two) {
    first = one;
    second = two;
  }

  /** Provides access to the first aggregated object. */
  public F getFirst() { return first; }

  /** Provides access to the second aggregated object. */
  public S getSecond() { return second; }

  /** @return true if the pair of objects are identical. */
  public boolean equals(Object other) {
    if (!(other instanceof GenericPair<?,?>)) return false;
    GenericPair<?,?> otherPair = (GenericPair<?,?>) other;
    return first.equals(otherPair.getFirst()) &&
           second.equals(otherPair.getSecond());
  }

  /** @return a hash code for the aggregate pair. */
  public int hashCode() {
    // XORing the hash codes to create a hash code for the pair.
    return first.hashCode() ^ second.hashCode();
  }

  /** @return the textual representation of aggregated objects. */
  public String toString() {
    return "[" + first + "," + second + "]";
  }
}