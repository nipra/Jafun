//+
/**
 * Demonstrate an Inner Child class
 * @author Ian F. Darwin, http://www.darwinsys.com/
 * @version $Id: InnerChild.java,v 1.3 2004/02/09 03:33:53 ian Exp $
 */
public class InnerChild {
	public class InnerInnerChild extends InnerChild {
	}
	public static void main(String[] argv) {
		// System.out.println(new InnerChild.InnerInnerChild()); // NOT how!
		InnerChild x = new InnerChild();
		System.out.println(x.new InnerInnerChild());
	}
}
//-
