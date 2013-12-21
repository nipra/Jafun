class Pair<A,B> {
    private final A left;
    private final B right;
    public Pair(A l, B r) { left=l; right=r; }
    public A left() { return left; }
    public B right() { return right; }
}

interface Exp<T> {
    public T eval();
}

class Interpreter {
    static Exp<Integer> lit(final int i) {
	return new Exp<Integer>() { public Integer eval() { return i; } };
    }
    static Exp<Integer> plus(final Exp<Integer> e1, final Exp<Integer> e2) {
	return new Exp<Integer>() { public Integer eval() {
		return e1.eval()+e2.eval();
	    } };
    }
    static <A,B> Exp<Pair<A,B>> pair(final Exp<A> e1, final Exp<B> e2) {
	return new Exp<Pair<A,B>>() { public Pair<A,B> eval() {
		return new Pair<A,B>(e1.eval(), e2.eval());
	    } };
    }
    static <A,B> Exp<A> left(final Exp<Pair<A,B>> e) {
	return new Exp<A>() { public A eval() { return e.eval().left(); } };
    }
    static <A,B> Exp<B> right(final Exp<Pair<A,B>> e) {
	return new Exp<B>() { public B eval() { return e.eval().right(); } };
    }
    public static void main(String[] args) {
	Exp<Integer> e = left(pair(plus(lit(2),lit(3)),lit(4)));
	System.out.println(e.eval());
	assert e.eval() == 5;
    }
}
