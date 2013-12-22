public class Latencies {
	public static void main(String[] args) {
		Thread[] threads;
		threads = new Thread[20];
		for (int i = 0; i < threads.length; i++) {
			threads[i] = new WorkerThread(10000000);
			threads[i].start();
		}
	}
}
