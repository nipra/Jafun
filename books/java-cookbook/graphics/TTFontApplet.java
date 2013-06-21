import java.awt.Container;

import javax.swing.JApplet;
import javax.swing.JLabel;

/** Create a TTFontDemo label in an Applet.
 * @version $Id: TTFontApplet.java,v 1.2 2004/03/26 03:34:31 ian Exp $
 */
public class TTFontApplet extends JApplet {

	/** Initialize the GUI */
	public void init() {

		String message = getParameter("message");
		if (message == null) 
			message = "TrueType Font Demonstration Applet";

		String fontFileName = getParameter("fontFileName");
		if (fontFileName == null)
			fontFileName = "Kellyag_.ttf";
		Container cp = getContentPane();
		try {
			cp.add(new TTFontDemo(fontFileName, message));
		} catch (Exception ex) {
			cp.add(new JLabel(ex.toString(), JLabel.CENTER));
			ex.printStackTrace();
		}
	}
}
