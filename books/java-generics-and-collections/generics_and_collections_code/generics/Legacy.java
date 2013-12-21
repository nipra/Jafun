import java.util.*;
class LegacyLibrary {
    public static void addItems(List list) {
        list.add(new Integer(1));  list.add("two");
    }
    public static List getItems() {
        List list = new ArrayList();
        list.add(new Integer(3));  list.add("four");
        return list;
    }
}
class NaiveClient {
    public static void processItems() {
        List<Integer> list = new ArrayList<Integer>();
        LegacyLibrary.addItems(list);  
        List<Integer> list2 = LegacyLibrary.getItems(); // unchecked
        // some time later ...
        int s = 0;
        for (int i : list) s += i; // class cast exception
        for (int i : list2) s += i; // class cast exception
    }
    public static void main(String[] args) {
	processItems();
    }
}
class WaryClient {
    public static void processItems() {
        List<Integer> list = new ArrayList<Integer>();
        List<Integer> view = Collections.checkedList(list, Integer.class);
        LegacyLibrary.addItems(view);  // class cast exception
        List<Integer> list2 = LegacyLibrary.getItems(); // unchecked
        for (int i : list2) {}  // class cast exception
        // sometime later ...
        int s = 0;
        for (int i : list) s += i;
        for (int i : list2) s += i;
    }
    public static void main(String[] args) {
	processItems();
    }
}
