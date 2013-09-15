class Node {                                                   // (1)
  private Object data;    // Data
  private Node   next;    // Next node

  // Constructor for initializing data and reference to the next node.
  Node(Object data, Node next) {
    this.data = data;
    this.next = next;
  }

  // Methods:
  public void   setData(Object obj) { data = obj; }
  public Object getData()           { return data; }
  public void   setNext(Node node)  { next = node; }
  public Node   getNext()           { return next; }
}
//______________________________________________________________________________
class LinkedList {                                             // (2)
  protected Node head = null;
  protected Node tail = null;

  // Methods:
  public boolean isEmpty() { return head == null; }
  public void insertInFront(Object dataObj) {
    if (isEmpty()) head = tail = new Node(dataObj, null);
    else head = new Node(dataObj, head);
  }
  public void insertAtBack(Object dataObj) {
    if (isEmpty())
      head = tail = new Node(dataObj, null);
    else {
      tail.setNext(new Node(dataObj, null));
      tail = tail.getNext();
    }
  }
  public Object deleteFromFront() {
    if (isEmpty()) return null;
    Node removed = head;
    if (head == tail) head = tail = null;
    else head = head.getNext();
    return removed.getData();
  }
}
//______________________________________________________________________________
class QueueByAggregation {                                     // (3)
  private LinkedList qList;

  // Constructor
  QueueByAggregation() {
    qList = new LinkedList();
  }

  // Methods:
  public boolean isEmpty() { return qList.isEmpty(); }
  public void enqueue(Object item) { qList.insertAtBack(item); }
  public Object dequeue() {
    if (qList.isEmpty()) return null;
    return qList.deleteFromFront();
  }
  public Object peek() {
    return (qList.isEmpty() ? null : qList.head.getData());
  }
}
//______________________________________________________________________________
class StackByInheritance extends LinkedList {                  // (4)
  public void push(Object item) { insertInFront(item); }
  public Object pop() {
    if (isEmpty()) return null;
    return deleteFromFront();
  }
  public Object peek() {
    return (isEmpty() ? null : head.getData());
  }
}
//______________________________________________________________________________
public class Client {                                           // (5)
  public static void main(String[] args) {
    String string1 = "Queues are boring to stand in!";
    int length1 = string1.length();
    QueueByAggregation queue = new QueueByAggregation();
    for (int i = 0; i<length1; i++)
      queue.enqueue(new Character(string1.charAt(i)));
    while (!queue.isEmpty())
      System.out.print(queue.dequeue());
    System.out.println();

    String string2 = "!no tis ot nuf era skcatS";
    int length2 = string2.length();
    StackByInheritance stack = new StackByInheritance();
    for (int i = 0; i<length2; i++)
      stack.push(new Character(string2.charAt(i)));
    stack.insertAtBack(new Character('!'));                     // (6)
    while (!stack.isEmpty())
      System.out.print(stack.pop());
    System.out.println();
  }
}