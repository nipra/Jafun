public class Mailbox {
    private String message;
    private boolean messagePending;

    /**
     * Places a message in the mailbox 
     */
    public synchronized void putMessage(String message) {
	while (messagePending) { //wait for consumers to consume
	    try {
		wait(); //block until notified
	    } catch (InterruptedException e) {
	    }
	}

	this.message = message; //storage message in mailbox 
	messagePending = true;  //raise flag on mailbox
	notifyAll();            //wake up any random consumer
    }

    /**
     * Retrieves a message from the mailbox
     * 
     * @return the message when available
     */
    public synchronized String getMessage() {
	while (!messagePending) { //wait for producer to produce
	    try {
		wait();
	    } catch (InterruptedException e) {
	    }
	}

	messagePending = false; //lower the flag on mailbox
	notifyAll();            //wake up any random producer

	return message;
    }
}
	    
