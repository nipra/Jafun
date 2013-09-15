
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Class with generic helper methods */
public class GenericUtilities {

  /** Comparator for reversing the order of a comparator */
  public static <T>
    Comparator<T> reverseOrder(final Comparator<? super T> cmp) {
    return new Comparator<T>() {
      public int compare(T t1, T t2) {
        return cmp.compare(t2, t1);
      }
    };
  }


  /** Convert Map to MultiMap */
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
}