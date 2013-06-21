/**
 * Test if you can use a null reference to find a static method.
 * @author Ian F. Darwin, http://www.darwinsys.com/
 * @version $Id: NullStaticMethod2.java,v 1.2 2004/02/09 03:33:54 ian Exp $
 */
public class NullStaticMethod2 {
	public static void invoke() {
		System.out.println("Invoked even though null");
	}
}
