import java.awt.*;
import javax.swing.*;

public class TVTest {

	/** "main program" for testing TV by itself. */
	public static void main(String av[]) {
		TD td = new TD();
		JFrame jf = new JFrame("Testing TV");
		TV tv = new TV(jf, td);
		Exam tex = td.curX;
		Q q;
		tex.addQuestion(q=new Q(3));
		q.setQText("How much is the universe?", false);
		tex.addQuestion(q=new Q(3));
		q.setQText("Does time fly like a banana?", false);
		tv.installQVs();

		jf.getContentPane().add(tv);
		jf.pack();
		jf.setVisible(true);
	}
}
