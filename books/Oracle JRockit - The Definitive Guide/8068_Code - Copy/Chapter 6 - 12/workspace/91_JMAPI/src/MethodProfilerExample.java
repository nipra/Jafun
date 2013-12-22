import java.io.StringWriter;
import java.lang.reflect.Method;

import com.bea.jvm.JVMFactory;
import com.bea.jvm.MethodProfileEntry;
import com.bea.jvm.ProfilingSystem;

public class MethodProfilerExample {
	public static void main(String[] args) throws Exception {
		String longString = generateLongString();
		ProfilingSystem profiler = JVMFactory.getJVM().getProfilingSystem();
		Method appendMethod = StringWriter.class.getMethod("append",
				CharSequence.class);
		MethodProfileEntry mpe = profiler.newMethodProfileEntry(appendMethod);
		mpe.setInvocationCountEnabled(true);
		mpe.setTimingEnabled(true);

		String total = doAppends(10000, longString);
		long invocationCount = mpe.getInvocations();
		long invocationTime = mpe.getTiming();
		System.out.println("Did " + invocationCount + " invocations");
		System.out.println("Average invocation time was "
				+ (invocationTime * 1000.0d) / invocationCount
				+ " microseconds");
		System.out.println("Total string length " + total.length());
	}

	private static String doAppends(int count, String longString) {
		StringWriter writer = new StringWriter();
		for (int i = 0; i < count; i++) {
			writer.append(longString);
		}
		return writer.toString();
	}

	private static String generateLongString() {
		StringWriter sw = new StringWriter(1000);
		for (int i = 0; i < 1000; i++) {
			// Build a string containing the characters
			// A to Z repeatedly.
			sw.append((char) (i % 26 + 65));
		}
		return sw.toString();
	}
}
