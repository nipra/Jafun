
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Class with generic helper methods */
public class GenericUtilities {

  /** Membership in an array. */
  // Non-generic version
  static boolean containsV1(Object key, Object[] array) {
    for(Object element : array)
      if(key.equals(element)) return true;
    return false;
  }
  // Generic version
  static <E> boolean containsV2(E key, E[] array) {
    for(E element : array)
      if(key.equals(element)) return true;
    return false;
  }
  static <E, K extends E> boolean containsV3(K key, E[] array) {
    for(E element : array)
      if(key.equals(element)) return true;
    return false;
  }

  /** Print an iterable, with one element on each line.
   *  Forexample, the list [e1, e2, e3] is printed as:
   *  e1
   *  e2
   *  e3
   */
  public static String toString(Iterable<?> list) {
    StringBuilder rep = new StringBuilder("");
    for (Object element : list) {
      rep.append(element + ", ");
    }
    if (rep.length() > 2)
      rep.delete(rep.length()-2, rep.length());
    return rep.toString();
  }

  public static void printList(Iterable<?> list) {
    for (Object element : list) {
      System.out.print(element);
    }
    System.out.println();
  }

  public static <E> void printList2(Iterable<E> list) {
    for (E element : list) {
      System.out.print(element);
    }
    System.out.println();
  }

//public static <E extends Comparable<E>> E max(List<E> list) {
  public static <E extends Comparable<? super E>> E max(List<? extends E> list) {
    E candidate = list.get(0);
    for (E element : list) {
      if (candidate.compareTo(element) < 0) {
        candidate = element;
      }
    }
    return candidate;
  }

//  public static <T extends Comparable<T>> T max(T obj1, T obj2) {
  public static <T extends Comparable<? super T>> T max(T obj1, T obj2) {
    T result = obj1;
    if (obj1.compareTo(obj2) < 0)  // OK
      result = obj2;
    return result;
  }


//public static <T>
//    T max(T obj1, T obj2, Comparator<T> comp) {
  public static <T>
    T max(T obj1, T obj2, Comparator<? super T> comp) {
    T result = obj1;
    if (comp.compare(obj1, obj2) < 0)  // OK
      result = obj2;
    return result;
  }

  /** Comparator for natural order */
  public static <T extends Comparable<? super T>>
    Comparator<T> naturalOrder() {
    return new Comparator<T>() {
      public int compare(T t1, T t2) {
        return t1.compareTo(t2);
      }
    };
  }

  /** Comparator for reversing the order of a comparator */
  public static <T>
    Comparator<T> reverseOrder(final Comparator<? super T> cmp) {
    return new Comparator<T>() {
      public int compare(T t1, T t2) {
        return cmp.compare(t2, t1);
      }
    };
  }

  public static <E> void fillWithFirstOne(List<E> inList) {
    E obj = inList.get(0);
    for (int i = 1; i < inList.size(); i++) {
      inList.set(i, obj);
    }
  }

  public static void fillWithFirst(List<?> inList) {
    fillWithFirstOne(inList);
//    Object obj = inList.get(0);
//    for (int i = 1; i < inList.size(); i++) {
//      inList.set(i, obj); // Compile-time error
//    }
  }

  // Convert Map to MultiMap
  public static <K,V> Map<V,List<K>> toMultiMap(Map<K,V> origMap) {
    Map<V, List<K>> multiMap = new HashMap<V,List<K>>();
    Collection<K> keys = origMap.keySet();
    for (K key : keys) {
      V value = origMap.get(key);
      List<K> valueList = multiMap.get(value);
      if (valueList == null) {
        valueList = new ArrayList<K>();
        multiMap.put(value, valueList);
      }
      valueList.add(key);
    }
    return multiMap;
  }

  public static void test() {
    GenericUtilities.printList(Arrays.asList(Arrays.asList(1, 2, 3),Arrays.asList(1.5, 2.5, 3.5))); // OK
  }

  public static void main(String[] args) {
    List<Integer> intList = Arrays.asList(1,2,3);
    fillWithFirst(intList);
    assert intList.toString().equals("[1, 1, 1]");

    List<String> strList = Arrays.asList("one","two","three");
    fillWithFirst(strList);
    assert strList.toString().equals("[one, one, one]");
  }
}