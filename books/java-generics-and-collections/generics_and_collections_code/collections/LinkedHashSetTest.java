package collections;

import java.util.Set;
import java.util.Collections;
import java.util.LinkedHashSet;

public class LinkedHashSetTest {

  public static void main(String[] args) {
    Set<Character> s2 = new LinkedHashSet<Character>(8);
    Collections.addAll(s2, 'a', 'b', 'j');
    // iterators of a LinkedHashSet return their elements in proper order:
    assert s2.toString().equals("[a, b, j]");
  }
}