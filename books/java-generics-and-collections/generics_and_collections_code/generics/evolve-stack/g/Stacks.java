class Stacks {
    public static <E> Stack<E> reverse(Stack<E> in) {
	Stack<E> out = new ArrayStack<E>();
        while (!in.empty()) {
	    E elt = in.pop();
	    out.push(elt);
	}
	return out;
    }
}

