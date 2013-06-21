/** StrCharAt - show String.charAt()
 * @author Ian F. Darwin, http://www.darwinsys.com/
 * @version $Id: StrCharAt.java,v 1.3 2004/02/09 03:34:03 ian Exp $
 */

public class StrCharAt {
    public static void main(String[] av) {
        String a = "A quick bronze fox lept a lazy bovine";
		for (int i=0; i < a.length(); i++)
			System.out.println("Char " + i + " is " + a.charAt(i));
	}
}
