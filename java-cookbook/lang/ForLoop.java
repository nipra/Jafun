/**
 * Check out "for" loop.
 * @author Ian F. Darwin, http://www.darwinsys.com/
 * @version $Id: ForLoop.java,v 1.5 2004/02/09 03:33:53 ian Exp $
 */
public class ForLoop {
	public static void main(String[] argv) {
		System.out.println("Starting...");
		// So what REALLY happens if a for loop's test condition is
		// never satisfied.
		for (int i=0; i<0; i++)
			System.out.println("Should not get here ");
		System.out.println("All done.");
	}
}
