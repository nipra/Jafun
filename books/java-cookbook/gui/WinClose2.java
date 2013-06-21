import javax.swing.*;

/* Show an example of closing a JFrame.
 * @author Ian Darwin
 * @version $Id: WinClose2.java,v 1.4 2002/02/16 23:22:48 ian Exp $
 */
public class WinClose2 {

	/* Main method */
	public static void main(String[] argv) {
		JFrame f = new JFrame("WinClose");
		f.setSize(200, 100);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
	}
}
