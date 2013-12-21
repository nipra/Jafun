import java.util.ArrayDeque;
import java.util.Arrays;

/** Executes tasks. */
public class TasksOnAStack {

  public static void main(String[] args) {
    Task[] taskArray = {
        new Task(200, "lunch"),
        new Task(200, "tea"),
        new Task(300, "dinner"),
        new Task(100, "breakfast"),
    };
    System.out.println("Array of tasks: " + Arrays.toString(taskArray));

    ArrayDeque<Task> fifoQueue = new ArrayDeque<Task>();
    for (Task task : taskArray)
      fifoQueue.offerLast(task);
    System.out.println("Queue before executing: HEAD->" + fifoQueue  + "<-TAIL");
    System.out.print("Doing tasks: ");
    while (!fifoQueue.isEmpty()) {
      Task task = fifoQueue.pollFirst();
      System.out.print(task + " ");
    }
    System.out.println();
    System.out.println("Queue after executing: " + fifoQueue);
  }
}