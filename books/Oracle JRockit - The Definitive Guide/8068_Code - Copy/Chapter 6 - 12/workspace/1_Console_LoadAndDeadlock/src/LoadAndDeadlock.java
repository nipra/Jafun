
public class LoadAndDeadlock {

	private static class AllocThread extends Thread {
		public void run() {
			while (true) {
				Thread.yield();
				try {
					sleep(30 * 1000);
				} catch (Exception e) {
				}

				for (int i = 0; i < 10000; i++) {
					char[] tmp = new char[1024 * 1024];
					tmp[1] = 'a';
				}
			}
		}
	}

	private static class LockerThread extends Thread {
		Object l1;
		Object l2;

		public void init(Object lock1, Object lock2) {
			l1 = lock1;
			l2 = lock2;
		}

		public void run() {
			while (true) {
				synchronized (l1) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
					}
					synchronized (l2) {
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
						}
						System.out.println("Got one!");

					}
				}
			}
		}

	}

	public static void main(String[] args) {
		AllocThread allocthread = new AllocThread();
		Object lock1 = new Object();
		Object lock2 = new Object();

		LockerThread first = new LockerThread();
		LockerThread second = new LockerThread();

		first.init(lock1, lock2);
		second.init(lock2, lock1);

		allocthread.start();
		first.start();
		second.start();

	}
}
