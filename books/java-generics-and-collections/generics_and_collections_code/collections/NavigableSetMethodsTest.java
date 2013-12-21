package collections;

import java.util.*;

public class NavigableSetMethodsTest {

  public static void main(String[] args) {
    NavigableSet<String> stringSet = new TreeSet<String>();
    Collections.addAll(stringSet, "abc", "cde", "x-ray" ,"zed");
    String last = stringSet.floor("x-ray");
    assert last.equals("x-ray");
    String secondToLast =
            last == null ? null : stringSet.lower(last);
    String thirdToLast =
            secondToLast == null ? null : stringSet.lower(secondToLast);
    assert thirdToLast.equals("abc");
    NavigableSet<String> headSet = stringSet.headSet(last, true);
    NavigableSet<String> reverseHeadSet = headSet.descendingSet();
    assert reverseHeadSet.toString().equals("[x-ray, cde, abc]");
    String conc = " ";
    for (String s : reverseHeadSet) {
      conc += s + " ";
    }
    assert conc.equals(" x-ray cde abc ");
    for (Iterator<String> itr = headSet.descendingIterator(); itr.hasNext(); ) {
      itr.next(); itr.remove();
    }
    assert headSet.isEmpty();
  }
}