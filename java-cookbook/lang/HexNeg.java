/**
 * Are all hex integers negative?
 * @author Ian F. Darwin, http://www.darwinsys.com/
 * @version $Id: HexNeg.java,v 1.4 2004/02/09 03:33:53 ian Exp $
 */
public class HexNeg {
	public static void main(String[] argv) {
		//+
		long data[] = { 0, 0x01, 0xff, 0x100, 0xffff, 0xffffff, 
			0x7fffffff, 0xffffffff };
		for (int i=0; i<data.length; i++)
			System.out.println("data["+i+"] = " + data[i]);
		//-
	}
}
