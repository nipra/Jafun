import java.util.*;

public class BFS {

    public static void main(String[] args) {
	long t0, t1;
	Node root = generateGraph(15000);
	List<Node> order;
	
	t0 = System.currentTimeMillis();
	order = breadthFirstSearchSlow(root);
	t1 = System.currentTimeMillis();
	System.out.println("Time slow: "+(t1-t0)+" ms (elements in path = " + order.size() + ")");

	t0 = System.currentTimeMillis();
	order = breadthFirstSearchFast(root);
	t1 = System.currentTimeMillis();
	System.out.println("Time fast: "+(t1-t0)+" ms (elements in path = " + order.size() + ")");
    }

    private static List<Node> breadthFirstSearchFast(Node root) {
	List<Node> order = new LinkedList<Node>();
	List<Node> queue = new LinkedList<Node>();
	Set<Node> visited = new HashSet<Node>();

	queue.add(root);
	visited.add(root);

	while (!queue.isEmpty()) {
	    Node node = queue.remove(0);
	    order.add(node);
	    for (Node succ : node.getSuccessors()) {
		if (!visited.contains(succ)) {
		    queue.add(succ);
		    visited.add(succ);
		}
	    }
	}

	return order;
    }

    private static List<Node> breadthFirstSearchSlow(Node root) {
	List<Node> order = new LinkedList<Node>();
	List<Node> queue = new LinkedList<Node>();
	
	queue.add(root);
	
	while (!queue.isEmpty()) {
	    Node node = queue.remove(0);
	    order.add(node);
	    for (Node succ : node.getSuccessors()) {
		if (!order.contains(succ) && !queue.contains(succ)) {
		    queue.add(succ);
		}
	    }
	}

	return order;
    }

    private static Node generateGraph(int size) {
	int nextIndex = 0;
	Node[] nodes = new Node[size];
	for (int i=0 ; i<size ; i++) {
	    nodes[i] = new Node(nextIndex++);
	}

	Random r = new Random(4711); //deterministic seed->deterministic graph	
	for (int i=0 ; i<size ; i++) {
	    int nsuccs = 1 + Math.abs(r.nextInt() & 7);
	    for (int j=0 ; j<nsuccs ; j++) {
		nodes[i].addSuccessor(nodes[Math.abs(r.nextInt() % size)]);
	    }
	}
	
	return nodes[0];
    }
    
    static class Node {
	int index;
	Set<Node> succs;
        Node(int index) {
	    this.index = index;
	    this.succs = new HashSet<Node>();
	}

	void addSuccessor(Node succ) {
	    succs.add(succ);
	}

	Set<Node> getSuccessors() {
	    return succs;
	}
    }
}

