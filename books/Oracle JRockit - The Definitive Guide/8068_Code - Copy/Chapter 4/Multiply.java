public class Multiply {
    private int somethingElse;

    public synchronized int multiply(int something) {
	return something * this.somethingElse;
    }

    public synchronized int multiplyExplicitSynchronization(int something) {
	synchronized(this) {
	    return something * this.somethingElse;
	}
    }
}
