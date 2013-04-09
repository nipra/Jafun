import java.awt.*;
import javax.swing.*;
import java.util.*;

public class PrintDraft {
	JFrame frm;
	Exam thisExam;
	PrintJob pjob;

	public PrintDraft(JFrame f, Exam m) {
		frm = f;
		thisExam = m;
	}

	public PrintDraft(Exam m) {
		this(null, m);
	}

	public void print() {

		Graphics pg = null;	// refers to current page

		System.out.println("Getting PrintJob");
		pjob = Toolkit.getDefaultToolkit().getPrintJob(frm,
			"Draft Printing", (Properties)null);
		if (pjob == null)          // User cancelled??
			return;

		// Just get and show dimenssions; should use in x,y calcs.
		Dimension pDim = pjob.getPageDimension();
		int pRes = pjob.getPageResolution();
		System.out.println("Page size " + pDim + "; Res " + pRes);

		// XXX RESTRUCTURE -- pass each question/answer string, maybe with
		// font.

		// Print up to "np" number of pages
		for (int pgNum=1; pgNum<=1; pgNum++) {
			System.out.println("Starting page # " + pgNum);
			pg = pjob.getGraphics();
			if (pg == null) 	// ??
				return;
			pg.setColor(Color.black);
			pg.setFont(new Font("Times", Font.PLAIN, 12));
			pg.drawString("Hello World", 100, 100);
			pg.drawString("Page" + pgNum, 300, 300);
			pg.dispose(); // flush page
			System.out.println("All done with page " + pgNum);
		}
		pjob.end();	// total end of print job.
		pjob = null;	// avoid redundant calls to pjob.end()
		return;
	}
}
