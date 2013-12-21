import java.util.*;

interface Stack<E> extends List<E> {
    public boolean empty();
    public void push(E elt);
    public E pop();
}
