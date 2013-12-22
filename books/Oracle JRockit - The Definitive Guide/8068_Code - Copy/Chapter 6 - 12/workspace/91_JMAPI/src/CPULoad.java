import com.bea.jvm.JVMFactory;

public class CPULoad {
	public static void main(String[] args) throws InterruptedException {
		for (int i = 0; i < 10; i++) {
			System.out.println(String.format("CPU load is %3.2f%%", JVMFactory
					.getJVM().getMachine().getCPULoad() * 100.0));
			Thread.sleep(1000);
		}
	}
}
