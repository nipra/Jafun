/*
 * Exception for TarFile and TarEntry.
 * $Id: TarException.java,v 1.3 1999/10/06 15:13:53 ian Exp $
 */
public class TarException extends java.io.IOException {
	public TarException() {
		super();
	}
	public TarException(String msg) {
		super(msg);
	}
}
