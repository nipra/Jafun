/**
 * Bad micro benchmark. Startup time will be almost all runtime
 */
import java.util.Random;

public class AnotherBadMicro {

    static Random r = new Random();
    static int sum;

    public static void main(String[] args) {
	long t0 = System.currentTimeMillis();
	int  s  = 0;
	for (int i=0 ; i<1000 ; i++) {
	    s += r.nextInt();
	}
	sum = s; //prevent dead code elim
	long t1 = System.currentTimeMillis();
	System.out.println("Time: "+(t1-t0)+" ms");
    }
}