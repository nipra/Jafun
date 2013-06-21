import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;

/**
 * Generalized Splash Screen, for applications (not Applets)
 * The application that creates me should start a thread that
 * sleeps a while, then calls my done() method.
 */
public class Splash extends JFrame {
	/** The name of the Image */
	protected String imName;
	/** The Image used to paint the screen */
	protected Image im;
	/** MediaTracker to load the image for us. */
	MediaTracker mt;

	/** Construct a Splash screen with the given image */
	Splash(JFrame parent, String imgName) {
		super("TestEdit initializing");
		im = Toolkit.getDefaultToolkit().getImage(imgName);
		mt = new MediaTracker(this);
		mt.addImage(im, 0);
		try {
			mt.waitForID(0);
		} catch(InterruptedException e) {
			System.err.println("Wonkey! INTR in waitForID!");
			return;
		}
		if (mt.isErrorID(0)) {
			System.err.println("Couldn't load Splash Image " +
				imgName);
			return;
		}
		getContentPane().add(new SplImage(im));
		//add(new SplImage(im));
		pack();

		addMouseListener(new MouseAdapter() {
			/** Let the user kill the Splash Screen anytime they want. */
			public void mouseClicked(MouseEvent e) {
				done();
			}
		});
	}

	/** Shut down this window.  Called from this class' mouseClicked(),
	 * but also from the main application.
	 */
	public void done() {
		setVisible(false);
		dispose();
	}
	
	/** The obligatory tiny test program. Normal use requires
	 * a thread in an AWT application; see JabaDex for example.
	 */
	public static void main(String a[]) {
		//(new Splash("Testing...", "JabaSpl.gif")).setVisible(true);
		JFrame frm = new JFrame("Testing");
		frm.setVisible(true);
		(new Splash(frm, "JabaSpl.gif")).setVisible(true);
	}
}

class SplImage extends Component {
	/** The Image */
	Image im;
	/** The width and height of the image */
	protected int width = 300, height = 250;

	/** Construct an SplImage. 
	 */
	SplImage(Image image) {
		im = image;
		width = im.getWidth(this);
		height = im.getHeight(this);
	}

	/** Compute our best size. Assumes the Image has been *fully
	 * loaded*, as via a MediaTracker's .waitFor() method.
	 */
	public Dimension getPreferredSize() {
		return new Dimension(width, height);
	}

	/** Called by AWT to paint the screen. 
	 */
	public void paint(Graphics g) {
		if (im == null) {
			g.setColor(Color.red);
			g.fillRect(0, 0, width, height);
		} else {
			g.drawImage(im, 0, 0, this);
		}
	}
}
