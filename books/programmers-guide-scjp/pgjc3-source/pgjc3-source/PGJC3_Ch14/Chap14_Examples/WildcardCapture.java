import java.util.List;

class WildcardCapture {


  public static void main(String[] args) {

    Node<?>                 anyNode;
    Node<? super Number>    supNumNode;

    Node<Integer> intNode = anyNode;                // (1) Compile-time error!
    Node<? extends Number> extNumNode = supNumNode; // (2) Compile-time error!
    anyNode.setData("Trash");                       // (3) Compile-time error!
    intNode = anyNode;
  }

  static void fillWithFirstV1(List<?> list) {
    Object firstElement = list.get(0);       // (1)
    for (int i = 1; i < list.size(); i++)
      list.set(i, firstElement);             // (2) Compile-time error
  }

  static <E> void fillWithFirstOne(List<E> list) {
    E firstElement = list.get(0);            // (3)
    for (int i = 1; i < list.size(); i++)
      list.set(i, firstElement);             // (4)
  }

  static void fillWithFirstV2(List<?> list) {
    fillWithFirstOne(list);
  }
}