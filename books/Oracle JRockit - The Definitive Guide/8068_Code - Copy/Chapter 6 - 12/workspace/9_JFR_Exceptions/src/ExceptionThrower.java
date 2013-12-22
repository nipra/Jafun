
public final class ExceptionThrower {
	public static void main(String[] args) throws Exception {
		new ExceptionThrower().loop();
	}

	private void loop() throws Exception {
		while(true) {
			try {
				Thread.sleep(2000);
				doStuff();
			} catch (ExceptionThrowerException e) {
				// Evilly swallow the exception.
			}
		}
	}
	
	private void doStuff() throws ExceptionThrowerException {
		// Having a few frames on the stack makes the traces, um, more interesting.
		throwMe();
	}
	
	private void throwMe() throws ExceptionThrowerException {
		throw new ExceptionThrowerException("Throw me!");
	}

}
