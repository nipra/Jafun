package collections;

import java.util.concurrent.Semaphore;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.Collection;
import java.util.Set;
import java.util.HashSet;

public class StoppableTaskQueue {
  private final int MAXIMUM_PENDING_OFFERS = Integer.MAX_VALUE;
  private final BlockingQueue<PriorityTask> taskQueue =
          new PriorityBlockingQueue<PriorityTask>();
  private boolean isStopped = false;
  private Semaphore semaphore = new Semaphore(MAXIMUM_PENDING_OFFERS);
  // return true if the task was successfully placed on the queue, false
  // if the queue has been shut down.
  public boolean addTask(PriorityTask task) {
    synchronized (this) {
      if (isStopped) return false;
      if (! semaphore.tryAcquire()) throw new Error("too many threads");
    }
    try {
      return taskQueue.offer(task);
    } finally {
      semaphore.release();
    }
  }
  // return the head task from the queue, or null if no task is available
  public PriorityTask getTask() {
    return taskQueue.poll();
  }
  // stop the queue, wait for producers to finish, then return the contents
  public Collection<PriorityTask> shutDown() {
    synchronized(this) { isStopped = true; }
    semaphore.acquireUninterruptibly(MAXIMUM_PENDING_OFFERS);
    Set<PriorityTask> returnCollection = new HashSet<PriorityTask>();
    taskQueue.drainTo(returnCollection);
    return returnCollection;
  }
}
