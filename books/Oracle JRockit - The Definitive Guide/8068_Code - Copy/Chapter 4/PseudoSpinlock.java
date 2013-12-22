public class PseudoSpinlock {

    private static final int LOCK_FREE  = 0;
    private static final int LOCK_TAKEN = 1;
    
    //memory position for lock, either free or taken
    static int lock;
    
    /**
     * try to atomically replace lock contents with "taken".
     * cmpxchg returns the old value of *lock.
     * If lock already was taken, this is a no-op.
     *
     * As long as we fail to set the taken bit,
     * we spin
     */
    public void lock() {    
	while (cmpxchg(LOCK_TAKEN, lock) == LOCK_TAKEN) 
	    ;//burn CPU cycles or do a yield	
    }
    
    /**
     * atomically replace lock contents with "free".
     */   
    public void unlock() {
	old = cmpxchg(LOCK_FREE, lock);
	//guard aganst recursive locks, i.e. the same lock being taken twice
	assert(old == LOCK_TAKEN);
    }
}

