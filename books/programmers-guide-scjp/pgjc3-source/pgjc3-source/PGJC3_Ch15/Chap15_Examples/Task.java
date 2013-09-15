import java.util.Comparator;

/** Represents a task. */
public class Task implements Comparable<Task> {
  private Integer taskNumber;
  private String taskName;

  public Task(Integer tp, String tn) {
    taskNumber = tp;
    taskName = tn;
  }
  public boolean equals(Object obj) { // Equality based on the task number.
    if (obj instanceof Task)
      return this.taskNumber.equals(((Task)obj).taskNumber);
    return false;
  }
  public int compareTo(Task task2) {  // Natural ordering based on the task number.
    return this.taskNumber.compareTo(task2.taskNumber);
  }
  public int hashCode() {             // Hash code based on the task number.
    return this.taskNumber.hashCode();
  }
  public String toString() {
    return taskNumber + "@" + taskName;
  }
  public String getTaskName() {
    return taskName;
  }

  // A total ordering based on *descending* order of task names (String).      (1)
  public static Comparator<Task> comparatorA() {
    return new Comparator<Task>() {
      public int compare(Task task1, Task task2) {
        return task2.getTaskName().compareTo(task1.getTaskName());
      }
    };
  }
  // A total ordering based on task numbers (Integer) and task names (String). (2)
  public static Comparator<Task> comparatorB() {
    return new Comparator<Task>() {
      public int compare(Task task1, Task task2) {
        if (!task1.taskNumber.equals(task2.taskNumber))
          return task1.taskNumber.compareTo(task2.taskNumber);
        if (!task1.taskName.equals(task2.taskName))
          return task1.getTaskName().compareTo(task2.getTaskName());
        return 0;
      }
    };
  }
}