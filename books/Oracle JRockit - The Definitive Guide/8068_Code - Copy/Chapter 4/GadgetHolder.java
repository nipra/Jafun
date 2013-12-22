public class GadgetHolder {
    
    private Gadget theGadget;
    
    public synchronized Gadget getGadget() {
	if (this.theGadget == null) {
	    this.theGadget = new Gadget();
	}
	return this.theGadget;
    }

    /**
     * Do not use double checked locking 
     */
    public synchronized Gadget getGadgetDoubleCheckedLocking() {
	if (this.theGadget == null) {
	    synchronized(this) {
		if (this.theGadget == null) {
		    this.theGadget = new Gadget();
		}
	    }
	}
	return this.theGadget;
    }

    private class Gadget {
    }
}
    