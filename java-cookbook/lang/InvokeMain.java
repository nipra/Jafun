//+
import java.lang.reflect.*;
//-

/**
 * Show loading a class and finding and calling its Main method.
 * @author Ian F. Darwin, http://www.darwinsys.com/
 * @version $Id: InvokeMain.java,v 1.3 2004/02/09 03:33:54 ian Exp $
 */
public class InvokeMain {
	public static void main(String[] argv) {
		//+
		try {
			// First, find the class.
			Class c = Class.forName("InvokeMain");	// RECURSION
			System.out.println(c);

			// Create the array of Argument Types
			Class[] argTypes = {
				argv.getClass(),	// array is Object!
			};

			// Now find the method
			Method m = c.getMethod("main", argTypes);
			System.out.println(m);

			// Create the actual argument array
			Object passedArgv[] = { argv };

			// Now invoke the method.
			m.invoke(null, passedArgv);

		} catch (Exception e) {
			System.err.println(e);
		}
		//-
	}
}
