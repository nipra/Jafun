public class WorkerThread extends Thread {
	public final static Logger LOGGER = Logger.getLogger();

	private int loopCount;

	public WorkerThread(int loopCount) {
		this.loopCount = loopCount;
	}

	public void run() {
		while (true) {
			int x = 0;
			int y = 0;
			for (int i = 0; i < loopCount; i++) {
				x += i;
				y %= 510;
				if (x % (this.loopCount/200) == 0) {
					Thread.yield();
				}
			}
			LOGGER.log("Thread reporting work done");
			Thread.yield();
		}
	}
}
