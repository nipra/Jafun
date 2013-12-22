
public class DoNothing {
	public static void main(String[] args) throws Exception {
		while (true) {
			Thread.sleep(1000);
			Thread.yield();
		}
	}
}
