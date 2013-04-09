/**
 * Reverse a string by character
 * @author Ian F. Darwin, http://www.darwinsys.com/
 * @version $Id: StringRevChar.java,v 1.3 2004/02/09 03:34:03 ian Exp $
 */
public class StringRevChar {
	public static void main(String[] argv) {
		//+
		String sh = "FCGDAEB";
		System.out.println(sh + " -> " + new StringBuffer(sh).reverse());
		//-
	}
}
