package regress;

import javax.swing.JFrame;

import com.darwinsys.swingui.SimpleHelp;

public class SimpleHelpTest {
	/** Test case */
	public static void main(String argv[]) {
		if (argv.length == 0)
			throw new IllegalArgumentException(
			"Usage: SimpleHelpTest helpFile");
		JFrame jf = new SimpleHelp("TESTING", argv[0]);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setVisible(true);
	}
}
