import java.util.concurrent.atomic.*;

public class AtomicAdderConcurrent {
    AtomicInteger counter = new AtomicInteger(17);
    
    public int add() {
	return counter.incrementAndGet();
    }
}
