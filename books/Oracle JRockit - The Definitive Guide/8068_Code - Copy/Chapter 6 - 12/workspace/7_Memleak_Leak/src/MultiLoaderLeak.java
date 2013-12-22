import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MultiLoaderLeak {
	public static void main(String[] args) throws ClassNotFoundException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		// Create three different loaders, A, B and C.
		// Load the Leak class in all three separately, and kick off two leaking, 
		// and one that isn't.
		CustomLoader loaderA = new CustomLoader("A");
		CustomLoader loaderB = new CustomLoader("B");
		CustomLoader loaderC = new CustomLoader("C");
		runMethod("startLeak", loaderA.loadClass("Leak"));
		runMethod("startNonLeak", loaderB.loadClass("Leak"));
		runMethod("startLeak", loaderC.loadClass("Leak"));
	}

	private static void runMethod(String method, Class<?> clazz) throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Method m = clazz.getMethod(method, new Class<?>[0]);
		m.invoke(null, new Object[0]);
	}
}
