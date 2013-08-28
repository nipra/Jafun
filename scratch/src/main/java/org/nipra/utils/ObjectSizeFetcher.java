package org.nipra.utils;

// http://stackoverflow.com/questions/52353/in-java-what-is-the-best-way-to-determine-the-size-of-an-object
// (nprabhak@nprabhak-mn ~/Projects/Java/Jafun/scratch)$ mvn jar:jar
// (nprabhak@nprabhak-mn ~/Projects/Java/Jafun/scratch)$ java -javaagent:target/scratch-1.0-SNAPSHOT.jar org.nipra.utils.ObjectSizeFetcher foo

import java.lang.instrument.Instrumentation;

public class ObjectSizeFetcher {
	private static Instrumentation instrumentation;

    public static void premain(String args, Instrumentation inst) {
        instrumentation = inst;
    }

    public static long getObjectSize(Object o) {
        return instrumentation.getObjectSize(o);
    }

	public static void main(String[] args) {
		String arg1 = args[0];
		System.out.println(arg1);
		for (String arg: args) {
			// System.out.println(arg);
			System.out.println(getObjectSize(arg));
		}
	}
}
