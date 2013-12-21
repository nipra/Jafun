import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

/** Executes tasks. */
public class TaskExecutor {

  public static void main(String[] args) {
    // Array with some tasks.                                                  (2)
    Task[] taskArray = {
        new Task(200, "lunch"), new Task(200, "tea"),
        new Task(300, "dinner"), new Task(100, "breakfast"),
    };
    System.out.println("Array of tasks: " + Arrays.toString(taskArray));

    // Priority queue using natural ordering                                   (3)
    PriorityQueue<Task> pq1 = new PriorityQueue<Task>();
    testPQ(taskArray, pq1);

    // Priority queue using a total ordering                                   (4)
    Comparator<Task> compA = Task.comparatorB();
    int initCap = 5;
    PriorityQueue<Task> pq2 = new PriorityQueue<Task>(initCap, compA);
    testPQ(taskArray, pq2);
  }

  static void testPQ(Task[] taskArray, PriorityQueue<Task> pq) {            // (5)
    // Load the tasks:                                                         (6)
    for (Task task : taskArray)
      pq.offer(task);
    System.out.println("Queue before executing tasks: " + pq);

    // Peek at the head:                                                       (7)
    System.out.println("Task at the head: " + pq.peek());

    // Do the tasks:                                                           (8)
    System.out.print("Doing tasks: ");
    while (!pq.isEmpty()) {
      Task task = pq.poll();
      System.out.print(task + " ");
    }
  }
}