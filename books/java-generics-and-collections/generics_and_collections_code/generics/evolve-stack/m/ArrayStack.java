import java.util.*;
@SuppressWarnings("unchecked")
class ArrayStack<E> implements Stack<E> {
    private List list;
    public ArrayStack() { list = new ArrayList(); }
    public boolean empty() { return list.size()==0; }
    public void push(E elt) { list.add(elt); }  // unchecked call
    public E pop() {
	Object elt = list.get(list.size()-1);
	list.remove(list.size()-1);
	return (E)elt;  // unchecked cast
    }
    public String toString() { return "stack"+list.toString(); }
}
