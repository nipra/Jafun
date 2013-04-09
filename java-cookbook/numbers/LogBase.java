/**
 * Log to arbitrary base
 * @author Ian F. Darwin, http://www.darwinsys.com/
 * @version $Id: LogBase.java,v 1.3 2004/02/09 03:33:57 ian Exp $
 */
public class LogBase {
	//+
	public static double log_base(double base, double value) {
		return Math.log(value) / Math.log(base);
	}
	//-
}
