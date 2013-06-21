/**
 * StringConvenience -- demonstrate java.lang.String convenience routine
 * @author Ian F. Darwin
 * @version $Id: StringConvenience.java,v 1.2 2004/02/23 02:37:34 ian Exp $
 */
public class StringConvenience {
	public static void main(String[] argv) {

		String pattern = ".*Q[^u]\\d+\\..*";
		String line = "Order QT300. Now!";
		if (line.matches(pattern)) {
			System.out.println(line + " matches \"" + pattern + "\"");
		} else {
			System.out.println("NO MATCH");
		}
	}
}
