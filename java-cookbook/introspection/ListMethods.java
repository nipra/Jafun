import java.lang.reflect.*;

/**
 * List the Constructors and methods
 * @author Ian F. Darwin, http://www.darwinsys.com/
 * @version $Id: ListMethods.java,v 1.3 2004/02/09 03:33:51 ian Exp $
 */
public class ListMethods {
	public static void main(String[] argv) throws ClassNotFoundException {
		if (argv.length == 0) {
			System.err.println("Usage: ListMethods className");
			return;
		}
		Class c = Class.forName(argv[0]);
		Constructor[] cons = c.getConstructors();
		printList("Constructors", cons);
		Method[] meths = c.getMethods();
		printList("Methods", meths);
	}
	static void printList(String s, Object[] o) {
		System.out.println("*** " + s + " ***");
		for (int i=0; i<o.length; i++)
			System.out.println(o[i].toString());
	}
}
