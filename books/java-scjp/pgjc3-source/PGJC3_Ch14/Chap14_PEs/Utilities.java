import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utilities {

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