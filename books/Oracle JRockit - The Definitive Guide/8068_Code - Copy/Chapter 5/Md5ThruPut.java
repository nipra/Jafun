import java.security.MessageDigest;
import java.util.Random;

/**
 * Bad microbenchmark - this includes the time required to compute
 * random numbers!
 */

public class Md5ThruPut {
    static MessageDigest algorithm;
    static Random r = new Random();
    static int ops;
    
    public static void main(String args[]) throws Exception {
	algorithm = MessageDigest.getInstance("MD5");
	algorithm.reset();
	long t0 = System.currentTimeMillis();
	test(100000);
	long t1 = System.currentTimeMillis();
	System.out.println((long)ops / (t1-t0) + " ops/ms ");
    }

    public static void test(int size) {
	for (int i=0 ; i<size ; i++) {
	    byte b[] = new byte[1024];
	    r.nextBytes(b);
	    digest(b);
	}
    }
    
    public static void digest(byte [] data) {
	algorithm.update(data);
	algorithm.digest();
	ops++;
    }
}
