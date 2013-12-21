import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GraphTraversal {

  public static void main(String[] args) {
    // (1) Given a directed graph with five vertices:
    Integer[][] neighbors = {
        {1, 3},  // Vertex 0
        {2},     // Vertex 1
        {4},     // Vertex 2
        {1, 2},  // Vertex 3
        {}       // Vertex 4
    };
    Map<Integer, Collection<Integer>> graph
                 = new HashMap<Integer, Collection<Integer>>();
    for (int i = 0; i < neighbors.length; i++) {
      graph.put(i, Arrays.asList(neighbors[i]));
    }

    // (2) Get the start vertex.
    int startVertex;
    try {
      startVertex = Integer.parseInt(args[0]);
    } catch (ArrayIndexOutOfBoundsException ive) {
      System.out.println("Usage: java GraphTraversal [0-4]");
      return;
    } catch (NumberFormatException nfe) {
      System.out.println("Usage: java GraphTraversal [0-4]");
      return;
    }

    Set<Integer> visitedSet = GraphTraversal.findVerticesOnPath(graph,
                                                                startVertex);
    System.out.print("Vertex " + startVertex + " is connected to " + visitedSet);
  }

  /**
   * Finds the vertices on a path from a given vertex in a directed graph.
   * In the map, the key is a vertex, and the value is the collection
   * containing its neighbours.
   */
  public static <N> Set<N> findVerticesOnPath(
                Map<N,Collection<N>> graph, N startVertex) {
    // (3) Create a stack for traversing the graph:
    MyStack<N> traversalStack = new MyStack<N>();

    // (4) Create a set for visited vertices:
    Set<N> visitedSet = new HashSet<N>();

    // (5) Push start vertex on the stack:
    traversalStack.push(startVertex);
    // (6) Handle each vertex found on the stack:
    while (!traversalStack.isEmpty()) {
      N currentVertex = traversalStack.pop();
      // (7) Check if current vertex has been visited.
      if (!visitedSet.contains(currentVertex)) {
        // (8) Add the current vertex to the visited set.
        visitedSet.add(currentVertex);
         // (9) Push neighbors of current vertex on to the stack.
        Collection<N> neighbors = graph.get(currentVertex);
        for (N neighbor : neighbors)
            traversalStack.push(neighbor);
      }
    }
    visitedSet.remove(startVertex);
    return visitedSet;
  }
}