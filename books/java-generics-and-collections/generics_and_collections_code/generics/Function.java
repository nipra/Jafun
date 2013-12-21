import java.util.*;
import java.lang.reflect.*;
abstract class Function<A,B,T extends Throwable> {
    public abstract B apply(A x) throws T;
    public List<B> applyAll(List<A> list) throws T {
        List<B> result = new ArrayList<B>(list.size());
        for (A x : list) result.add(apply(x));
        return result;
    }
    public static void main (String... args) {
        Function<String,Integer,Error> length =
            new Function<String,Integer,Error>() {
                public Integer apply(String s) {
                    return s.length();
                }
            };
        Function<String,Class<?>,ClassNotFoundException> forName =
            new Function<String,Class<?>,ClassNotFoundException>() {
                public Class<?> apply(String s)
                    throws ClassNotFoundException
                {
                    return Class.forName(s);
                }
            };  
        Function<String,Method,Exception> getRunMethod =
            new Function<String,Method,Exception>() {
                public Method apply(String s)
                    throws ClassNotFoundException,NoSuchMethodException
                {
                    return Class.forName(s).getMethod("run");
                }
            };
        List<String> strings = Arrays.asList(args);
        System.out.println(length.applyAll(strings));

        try { System.out.println(forName.applyAll(strings)); }
        catch (ClassNotFoundException e) { System.out.println(e); }

        try { System.out.println(getRunMethod.applyAll(strings)); }
        catch (ClassNotFoundException e) { System.out.println(e); }
        catch (NoSuchMethodException e) { System.out.println(e); }
        catch (RuntimeException e) { throw e; }
        catch (Exception e) { assert false; }

    }
}


