import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;

/** Simple test case for QView */
public class QViewTest {
	public static void main(String a[]) {
		JFrame f = new JFrame("QView Test");

		JTabbedPane cm = new JTabbedPane();
		f.setContentPane(cm);

		Q q = new Q(4);
		QView qv = new QView(q);
		cm.addTab("0", qv);
		q.setQText("To be or not to be, that is the question", true);
		q.setAnsText(0, "Hello world", true);
		q.setAnsText(1, "Goodbye cruel world", true);
		q.setAnsText(2, "Settle it with a bare bodkin, princikins", true);
		q.setAnsText(3, "42", true);
		q.setAns(2, false);

		q = new Q(4);
		q.setQText("Whether 'tis nobler in the mind to suffer the slings and arrows of outrageous fortune,", true);
		q.setAnsText(0, "Hello world", true);
		q.setAnsText(1, "Goodbye cruel world", true);
		q.setAnsText(2, "Settle it with a bare bodkin, princikins", true);
		q.setAnsText(3, "42", true);
		q.setAns(3, false);
		qv = new QView(q);
		cm.addTab("1", qv);

		f.setSize(600, 400);
		f.setLocation(100, 100);
		f.setVisible(true);
		f.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				System.exit(0);
			}
		});
	}
}
