package collections;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.Delayed;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.BlockingQueue;
import java.util.Set;
import java.util.HashSet;
import static collections.StaticTestData.*;

public class DelayedTask implements Delayed {
  public final static long MILLISECONDS_IN_DAY = 60 * 60 * 24 * 1000;
  private long endTime;     // in milliseconds after January 1, 1970
  private Task task;
  DelayedTask(Task t, int daysDelay) {
    endTime = System.currentTimeMillis() + daysDelay * MILLISECONDS_IN_DAY;
    task = t;
  }
  public long getDelay(TimeUnit unit) {
    long remainingTime = endTime - System.currentTimeMillis();
    return unit.convert(remainingTime, TimeUnit.MILLISECONDS);
  }
  public int compareTo(Delayed t) {
    long thisDelay = getDelay(TimeUnit.MILLISECONDS);
    long otherDelay = t.getDelay(TimeUnit.MILLISECONDS);
    return (thisDelay < otherDelay) ? -1 : (thisDelay > otherDelay) ? 1 : 0;
  }
  Task getTask() { return task; }
  public static void main(String[] args) {
    BlockingQueue<DelayedTask> reminderQueue = new DelayQueue<DelayedTask>();
    reminderQueue.offer(new DelayedTask (databaseCode, 1));
    reminderQueue.offer(new DelayedTask (interfaceCode, 2));

    // now get the first reminder task that is ready to be processed
    DelayedTask t1 = reminderQueue.poll();
    if (t1 == null) {
      // no reminders ready yet
    } else {
      // process t1
    }
    Set<DelayedTask> delayedTaskSet1 = new HashSet<DelayedTask>();
    delayedTaskSet1.addAll(reminderQueue);
    Set<DelayedTask> delayedTaskSet2 = new HashSet<DelayedTask>();
    reminderQueue.drainTo(delayedTaskSet2);

  }
}