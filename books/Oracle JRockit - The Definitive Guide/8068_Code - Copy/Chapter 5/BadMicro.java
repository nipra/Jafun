/**
 * Bad micro benchmark. Won't perform well on adaptive runtimes
 * without on-stack replacement
 */
public class BadMicro {
    public static void main(String[] args) {
	long t0 = System.currentTimeMillis();
	for (int i=0 ; i<1000000 ; i++) {
	    methodToMeasure();
	}
	long t1 = System.currentTimeMillis();
	System.out.println("Time: "+(t1-t0)+" ms");
    }

    public static void methodToMeasure() {
	//insert benchmark payload code here 
    }
}