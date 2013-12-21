package collections;

import java.util.Comparator;
import java.util.Queue;
import java.util.PriorityQueue;
import static collections.StaticTestData.*;

public class PriorityQueueTest {

  public static void main(String[] args) {
    final int INITIAL_CAPACITY = 10;
    Comparator<PriorityTask> priorityComp = new Comparator<PriorityTask>() {
      public int compare(PriorityTask o1, PriorityTask o2) {
        return o1.getPriority().compareTo(o2.getPriority());
      }
    };
    Queue<PriorityTask> priorityQueue =
            new PriorityQueue<PriorityTask>(INITIAL_CAPACITY, priorityComp);
    priorityQueue.add(new PriorityTask(mikePhone, Priority.MEDIUM));
    priorityQueue.add(new PriorityTask(paulPhone, Priority.HIGH));

    PriorityTask nextTask = priorityQueue.poll();
  }
}