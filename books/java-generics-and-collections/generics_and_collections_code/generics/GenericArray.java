import java.util.List;
import java.util.AbstractList;
import java.lang.reflect.Array;
class GenericArray {
    public static <T> T[] newInstance(Class<T> c, int size) {
	return (T[])Array.newInstance(c, size);  // unchecked
    }
    public static <T> Class<T> getComponentType(T[] a) {
	return (Class<T>)a.getClass().getComponentType();  // unchecked
    }
    public static <T> T[] newInstance(T[] arr, int size) {
	return newInstance(getComponentType(arr), size);
    }
    public static <T> Class<T> getClass(T o) {
	return (Class<T>)o.getClass();  // unchecked
    }
    public static <T> T newInstance(T obj) throws InstantiationException, IllegalAccessException {
	return (T)obj.getClass().newInstance();  // unchecked
    }
}
