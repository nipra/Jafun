
public class UsingRecBounds {
  public static void main(String[] args) {
    BiNode<Integer> biNode0 = new BiNode<Integer>(2011, null,  null);
    BiNode<Integer> biNode1 = new BiNode<Integer>(2009, null,  null);
    BiNode<Integer> biNode2 = new BiNode<Integer>(2012, null, biNode0);
    BiNode<Integer> biNode = new BiNode<Integer>(2010, biNode2, biNode1);
    IBiLink<Integer> biNodeL = biNode.getPrevious();
    IMonoLink<Integer> monoNodeNext = biNode.getNext();
    IBiLink<Integer> biNodeNext = (IBiLink<Integer>) monoNodeNext; // Cast necessary!
    System.out.println(biNode);
    System.out.println(biNodeNext.getPrevious().getData());

    RecBiNode<Integer> recBiNode0 = new RecBiNode<Integer>(2011, null, null);
    RecBiNode<Integer> recBiNode1 = new RecBiNode<Integer>(2009, null, null);
    RecBiNode<Integer> recBiNode2 = new RecBiNode<Integer>(2012, null, recBiNode0);
    RecBiNode<Integer> recBiNode = new RecBiNode<Integer>(2010, recBiNode2, recBiNode1);
    RecBiNode<Integer> recBiNodeL = recBiNode.getPrevious();
    RecBiNode<Integer> recBiNodeR = recBiNode.getNext();
    System.out.println(recBiNode);
    System.out.println(recBiNodeR.getPrevious().getData());
    traverseBinTree(biNode);
  }

  public static <T> void traverseBinTree(RecBiNode<T> root) {     // (2)
    if (root.getPrevious() != null)
      traverseBinTree(root.getPrevious());
    System.out.print(root.getData() + ", ");
    if (root.getNext() != null)
      traverseBinTree(root.getNext());             // No cast necessary!
  }

  public static <T> void traverseBinTree(IBiLink<T> root) {       // (1)
    if (root.getPrevious() != null)
      traverseBinTree(root.getPrevious());
    System.out.print(root.getData() + ", ");
    if (root.getNext() != null)
      traverseBinTree((IBiLink<T>)root.getNext());     // Cast necessary.
  }
}