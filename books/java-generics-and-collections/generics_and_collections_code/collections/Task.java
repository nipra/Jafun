package collections;

public abstract class Task implements Comparable<Task> {
  protected Task() {}
  public boolean equals(Object o) {
    if (o instanceof Task) {
      return toString().equals(o.toString());
    } else return false;
  }
  public int compareTo(Task t) {
    return toString().compareTo(t.toString());
  }
  public int hashCode() {
    return toString().hashCode();
  }
  public abstract String toString();
}
