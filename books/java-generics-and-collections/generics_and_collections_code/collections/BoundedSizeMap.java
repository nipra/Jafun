package collections;

import java.util.LinkedHashMap;
import java.util.Map;

public class BoundedSizeMap extends LinkedHashMap {
  private int maxEntries;
  public BoundedSizeMap(int maxEntries) {
    super(16, 0.75f, true);
    this.maxEntries = maxEntries;
  }
  protected boolean removeEldestEntry(Map.Entry eldest) {
    return size() > maxEntries;
  }
}
