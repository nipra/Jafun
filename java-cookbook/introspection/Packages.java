/**
 * Show the Packages. Requires JDK1.2.
 * @author Ian F. Darwin, http://www.darwinsys.com/
 * @version $Id: Packages.java,v 1.4 2004/02/09 03:33:51 ian Exp $
 */
public class Packages {
	public static void main(String[] argv) {
		//+
		java.lang.Package[] all = java.lang.Package.getPackages();
		for (int i=0; i<all.length; i++)
			System.out.println(all[i]);
		//-
	}
}
