import junit.framework.*;

/** A simple test case for XXX.
 * This class' name should be XXXTest, where XXX is the class it tests.
 */
public class junitTest extends TestCase {

	protected String f;

	/** JUnit test classes require this constructor */
	public junitTest(String name) {
		super(name);
	}

	public void testXXX() {
		f = "...";
		assertEquals("Ian Darwin", f);
	}
}
