import java.util.*;

interface ListString extends List<String> {}

class ListStrings {
  public static ListString wrap(final List<String> list) {
    class Random extends AbstractList<String>
      implements ListString, RandomAccess
    {
      public int size() { return list.size(); }
      public String get(int i) { return list.get(i); }
      public String set(int i, String s) { return list.set(i,s); }
      public String remove(int i) { return list.remove(i); }
      public void add(int i, String s) { list.add(i,s); }
    }
    class Sequential extends AbstractSequentialList<String>
      implements ListString
    {
      public int size() { return list.size(); }
      public ListIterator<String> listIterator(int index) {
        final ListIterator<String> it = list.listIterator(index);
        return new ListIterator<String>() {
          public void add(String s) { it.add(s); }
          public boolean hasNext() { return it.hasNext(); }
          public boolean hasPrevious() { return it.hasPrevious(); }
          public String next() { return it.next(); }
          public int nextIndex() { return it.nextIndex(); }
          public String previous() { return it.previous(); }
          public int previousIndex() { return it.previousIndex(); }
          public void remove() { it.remove(); }
          public void set(String s) { it.set(s); }
        };
      }
    }
    return list instanceof RandomAccess ? new Random() : new Sequential();
  }
}

class ArrayListString extends ArrayList<String> implements ListString {
  public ArrayListString() { super(); }
  public ArrayListString(Collection<? extends String> c) { super(c); }
  public ArrayListString(int capacity) { super(capacity); }
}

class Test {
  public static void testDelegation() {
    List<? extends List<?>> lists =
      Arrays.asList(
        ListStrings.wrap(Arrays.asList("one","two")),
        Arrays.asList(3,4),
        Arrays.asList("five","six"),
        ListStrings.wrap(Arrays.asList("seven","eight"))
      );
    ListString[] array = new ListString[2];
    int i = 0;
    for (List<?> list : lists)
      if (list instanceof ListString)
        array[i++] = (ListString)list;
    assert Arrays.toString(array).equals("[[one, two], [seven, eight]]");
  }
  public static void testInheritance() {
    List<? extends List<?>> lists =
      Arrays.asList(
        new ArrayListString(Arrays.asList("one","two")),
        Arrays.asList(3,4),
        Arrays.asList("five","six"),
        new ArrayListString(Arrays.asList("seven","eight"))
      );
    ListString[] array = new ListString[2];
    int i = 0;
    for (List<?> list : lists)
      if (list instanceof ListString)
        array[i++] = (ListString)list;
    assert Arrays.toString(array).equals("[[one, two], [seven, eight]]");
  }
  public static void main(String[] args) {
    testDelegation();
    testInheritance();
  }
}
