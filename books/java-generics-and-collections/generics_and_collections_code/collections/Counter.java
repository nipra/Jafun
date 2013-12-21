package collections;

import java.util.Iterator;

public class Counter implements Iterable<Integer> {
  private int count;
  public Counter(int count) { this.count = count; }
  public Iterator<Integer> iterator() {
    return new Iterator<Integer>() {
      private int i = 0;
      public boolean hasNext() { return i < count; }
      public Integer next() { i++; return i; }
      public void remove(){ throw new UnsupportedOperationException(); }
    };
  }

  public static void main(String[] args){
    int total = 0;
    for (int i : new Counter(3)) {
      total += i;
    }
    assert total == 6;
  }
}
