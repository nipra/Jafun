public class AtomicAdder {
    int counter = 17;
    
    public int add() {
	synchronized(this) {
	    return ++counter;
	}
    }
}
