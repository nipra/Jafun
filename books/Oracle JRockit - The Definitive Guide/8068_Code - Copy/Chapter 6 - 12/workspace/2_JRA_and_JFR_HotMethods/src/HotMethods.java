
public class HotMethods {
	/*
	 * You are not allowed to edit this file, the error is not in here.
	 */
	public static void main(String[] args) {		
		long start;
		long stop;
		int counter = 0;
		long totaltime = 0;

		while (true) {
			start = System.currentTimeMillis();
			Initiator i1 = new Initiator();
			Initiator i2 = new Initiator();
			i1.initiate(3);
			i2.initiate(5);
			int similars = i1.countSimilars(i2);
			System.out.println("Similars: " + similars);
			stop = System.currentTimeMillis();
			totaltime += (stop - start);
			counter++;
			System.out.println("Times - this round: " + (stop - start) + " ms. Average time: " + (totaltime/counter));
		}
	}

}
