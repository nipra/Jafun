import java.security.MessageDigest;
import java.util.Random;

/**
 * Better microbenchmark
 */

public class Md5ThruPutBetter {
    static MessageDigest algorithm;
    static Random r = new Random();
    static int ops;
    static byte[][] input;
    
    public static void main(String args[]) throws Exception {
	algorithm = MessageDigest.getInstance("MD5");
	algorithm.reset();
	generateInput(100000);
	long t0 = System.currentTimeMillis();
	test();
	long t1 = System.currentTimeMillis();
	System.out.println((long)ops / (t1-t0) + " ops/ms");
    }

    public static void generateInput(int size) {
	input = new byte[size][];
	for (int i=0 ; i<size ; i++) {
	    input[i] = new byte[1024];
	    r.nextBytes(input[i]);
	}
    }
    
    public static void test() {
	for (int i=0 ; i<input.length ; i++) {
	    digest(input[i]);
	}
    }
    
    public static void digest(byte [] data) {
	algorithm.update(data);
	algorithm.digest();
	ops++;
    }
}
