/**
 * Read from Standard Input
 * @author	Ian F. Darwin, http://www.darwinsys.com/
 * @version 	$Id: ReadStdin.java,v 1.3 2004/02/08 23:57:29 ian Exp $
 */
public class ReadStdin {
	/** Simple test case */
	public static void main(String[] ap) {
		//+
		int b = 0;
		try {
			b = System.in.read();
		} catch (Exception e) {
			System.out.println("Caught " + e);
		}
		System.out.println("Read this data: " + (char)b);
		//-
	}
}
