package collections;

public final class PriorityTask implements Comparable<PriorityTask> {
  private final Task task;
  private final Priority priority;
  PriorityTask(Task task, Priority priority) {
    this.task = task;
    this.priority = priority;
  }
  public Task getTask() { return task; }
  public Priority getPriority() { return priority; }
  public int compareTo(PriorityTask pt) {
    int c = priority.compareTo(pt.priority);
    return c != 0 ? c : task.compareTo(pt.task);
  }
  public boolean equals(Object o) {
    if (o instanceof PriorityTask) {
      PriorityTask pt = (PriorityTask)o;
      return task.equals(pt.task) && priority.equals(pt.priority);
    } else return false;
  }
  public int hashCode() { return task.hashCode(); }
  public String toString() { return task + ": " + priority; }
}
