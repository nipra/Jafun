import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/** Class to create a password file */
public final class MakePasswordFile {

  public static void main (String[] args) throws IOException {
    Map<String, Integer> pwStore = new TreeMap<String, Integer>();
    pwStore.put("tom", "123".hashCode());
    pwStore.put("dick", "456".hashCode());
    pwStore.put("harry", "789".hashCode());

    PrintWriter destination = new PrintWriter(new FileWriter("pws.txt"));
    Set<Map.Entry<String, Integer>> pwSet = pwStore.entrySet();
    for (Map.Entry<String, Integer> entry : pwSet) {
      // Format: login password
      destination.printf("%s %s%n", entry.getKey(), entry.getValue());
    }
    destination.flush();
    destination.close();
  }
}