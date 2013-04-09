import javax.swing.JButton;
import javax.swing.JFrame;

/** Just a Frame
 * @version $Id: JFrameSimple.java,v 1.2 2004/02/23 03:39:22 ian Exp $
 */
public class JFrameSimple extends JFrame {
	JButton quitButton;

	/** Construct the object including its GUI */
	public JFrameSimple() {
		super("JFrameSimple");

		getContentPane().add(quitButton = new JButton("Exit"));

		// Set up so that "Close" will exit the program
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		pack();
	}

	public static void main(String[] args) {
		new JFrameSimple().setVisible(true);
	}
}
