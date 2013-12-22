import java.util.*;

public class Initiator {
	private Collection<Integer> list;

	public Initiator() {
		list = new LinkedList<Integer>();
	}

	// Creates a list of all integers 1-20,000 that aren't 0 mod seed
	public void initiate(int seed) {
		list.clear(); // Start fresh
		for (int i = 1; i < 20000; i++) {
			if ((i % seed) != 0)
				list.add(i);
		}
	}

	protected Collection<Integer> getList() {
		return list;
	}

	public int countSimilars(Initiator other) {
		int count = 0;
		for (Integer i : list) {
			if (other.getList().contains(i)) {
				count++;
			}
		}
		return count;
	}
}
