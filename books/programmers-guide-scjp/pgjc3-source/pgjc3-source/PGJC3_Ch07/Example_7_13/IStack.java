interface IStack {
  void push(Object item);
  Object pop();
}

class StackImpl implements IStack {
  protected Object[] stackArray;
  protected int tos;

  public StackImpl(int capacity) {
    stackArray = new Object[capacity];
    tos = -1;
  }

  public void push(Object item)
  { stackArray[++tos] = item; }

  public Object pop() {
    Object objRef = stackArray[tos];
    stackArray[tos] = null;
    tos--;
    return objRef;
  }

  public Object peek() { return stackArray[tos]; }
}

interface ISafeStack extends IStack {
  boolean isEmpty();
  boolean isFull();
}

class SafeStackImpl extends StackImpl implements ISafeStack {

  public SafeStackImpl(int capacity) { super(capacity); }
  public boolean isEmpty() { return tos < 0; }
  public boolean isFull() { return tos >= stackArray.length - 1; }
}