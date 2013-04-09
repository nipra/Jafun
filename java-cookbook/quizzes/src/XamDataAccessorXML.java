import java.io.*;
import java.util.*;

/** TestEdit Load/Save model - XML version.
 * Not written yet, but at least I thought of it. :-)
 * DOM will probably be a good approach here.
 */
public class XamDataAccessorXML extends XamDataAccessor {

	public XamDataAccessorXML() {
		super();
	}

    /** load one file, given an open BufferedReader */
    public Exam load(BufferedReader is) throws IOException {
		throw new IllegalArgumentException(
			"XamDataAccessorXML cannot yet LOAD files");
		// TODO: use DOM to parse it.
    }

	public void save(PrintWriter out, Exam model) {
		throw new IllegalArgumentException(
			"XamDataAccessorXML cannot yet SAVE files");
		// TODO convert to DOM, and end with
		// tree.write(out);
	}
}
