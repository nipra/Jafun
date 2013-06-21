/** A class to test Complex Numbers. 
 * @author Ian F. Darwin, http://www.darwinsys.com/
 * @version $Id: ComplexDemo.java,v 1.6 2004/05/13 22:28:59 ian Exp $
 */
public class ComplexDemo {
	/** The program */
	public static void main(String[] args) {
		Complex c = new Complex(3,  5);
		Complex d = new Complex(2, -2);
		System.out.println(c);
		System.out.println(c + ".getReal() = " + c.getReal());
		System.out.println(c + " + " + d + " = " + c.add(d));
		System.out.println(c + " + " + d + " = " + Complex.add(c, d));
		System.out.println(c + " * " + d + " = " + c.multiply(d));
		System.out.println(Complex.divide(c, d));
	}
}
