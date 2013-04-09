import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/** Just a Frame
 * @version $Id: JFrameDemo.java,v 1.13 2004/03/20 20:44:05 ian Exp $
 */
public class JFrameDemo extends JFrame {
	JButton quitButton;

	/** Construct the object including its GUI */
	public JFrameDemo() {
		super("JFrameDemo");
		Container cp = getContentPane();
		cp.add(quitButton = new JButton("Exit"));

		// Set up so that "Close" will exit the program, 
		// not just close the JFrame.
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// This "action handler" will be explained later in the chapter.
		quitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				dispose();
				System.exit(0);
			}
		});
			
		pack();
	}
	public static void main(String[] args) {
		new JFrameDemo().setVisible(true);
	}
}
