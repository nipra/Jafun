import java.io.IOException;
import java.io.PrintWriter;

/**
 * Callback so that ResultsDecorator can call invoker to handle redirections etc.
 * @version $Id: ResultsDecoratorPrinter.java,v 1.1 2004/03/26 02:39:33 ian Exp $
 */
public interface ResultsDecoratorPrinter {
	
	void print(String line) throws IOException;
	
	void println(String line) throws IOException;
	
	void println() throws IOException;
	
	PrintWriter getPrintWriter();
}
