import com.bea.jvm.JVMFactory;
import com.bea.jvm.NIC;

public class NICInfo {
	public static void main(String[] args) {
		for (NIC nic : JVMFactory.getJVM().getMachine().getNICs()) {
			System.out.println(nic.getDescription() + " MAC:" + nic.getMAC()
					+ " MTU:" + nic.getMTU());
		}
	}
}
