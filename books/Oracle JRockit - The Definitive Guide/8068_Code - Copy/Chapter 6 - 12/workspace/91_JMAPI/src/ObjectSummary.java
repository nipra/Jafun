import com.bea.jvm.DiagnosticCommand;
import com.bea.jvm.JVMFactory;

public class ObjectSummary {
	public static void main(String[] args)
			throws InterruptedException {
		DiagnosticCommand dc = JVMFactory.getJVM()
				.getDiagnosticCommand();
		String output = dc.execute("print_object_summary");
		System.out.println(output);
	}
}
