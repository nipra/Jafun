public class WorkerThread extends Thread {
	public final static Logger LOGGER = Logger.getLogger();

	private int loopCount;

	public WorkerThread(int loopCount) {
		this.loopCount = loopCount;
	}

	private WorkEvent event = new WorkEvent(CustomEventLatencies.WORK_EVENT_TOKEN);
	public void run() {
		while (true) {
			event.reset();
			event.begin();
			int x = 0;
			int y = 0;
			for (int i = 0; i < loopCount; i++) {
				x += i;
				y %= 510;
				if (x % (this.loopCount/200) == 0) {
					Thread.yield();
				}
			}
			event.end();
			event.commit();
			LOGGER.log("Thread reporting work done");
			Thread.yield();
		}
		
	}
}
