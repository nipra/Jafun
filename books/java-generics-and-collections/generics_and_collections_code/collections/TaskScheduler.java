package collections;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ListIterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class TaskScheduler {
  private List<StoppableTaskQueue> schedule;
  private final int FORWARD_PLANNING_DAYS = 365;
  public TaskScheduler() {
    List<StoppableTaskQueue> temp = new ArrayList<StoppableTaskQueue>();
    for (int i = 0 ; i < FORWARD_PLANNING_DAYS ; i++) {
      temp.add(new StoppableTaskQueue());
    }
    schedule = new CopyOnWriteArrayList<StoppableTaskQueue>(temp);    //1
  }
  public PriorityTask getTask() {
    for (StoppableTaskQueue daysTaskQueue : schedule) {
      PriorityTask topTask = daysTaskQueue.getTask();
      if (topTask != null) return topTask;
    }
    return null;    // no outstanding tasks - at all!?
  }
  // at midnight, remove and shut down the queue for day 0, assign its tasks
  // to the new day 0, and create a new day's queue at the planning horizon
  public void rollOver() throws InterruptedException{
    StoppableTaskQueue oldDay = schedule.remove(0);
    Collection<PriorityTask> remainingTasks = oldDay.shutDown();
    StoppableTaskQueue firstDay = schedule.get(0);
    for (PriorityTask t : remainingTasks) {
      firstDay.addTask(t);
    }
    StoppableTaskQueue lastDay = new StoppableTaskQueue();
    schedule.add(lastDay);
  }
  public void addTask(PriorityTask task, int day) {
    if (day < 0 || day >= FORWARD_PLANNING_DAYS)
      throw new IllegalArgumentException("day out of range");
    StoppableTaskQueue daysTaskQueue = schedule.get(day);
    if (daysTaskQueue.addTask(task)) return;                        //2
    // StoppableTaskQueue.addTask returns false only when called on
    // a queue that has been shut down. In that case, it will also
    // have been removed by now, so it's safe to try again.
    if (! schedule.get(0).addTask(task)) {
      throw new IllegalStateException("failed to add task " + task);
    }
  }
  ListIterator<StoppableTaskQueue> getSubSchedule(int startDay, int endDay) {
    return schedule.subList(startDay, endDay).listIterator();
  }
}
