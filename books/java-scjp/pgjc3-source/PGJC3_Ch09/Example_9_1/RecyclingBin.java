class HeavyItem {                                 // (1)
  int[]     itemBody;
  String    itemID;
  HeavyItem nextItem;

  HeavyItem(String ID, HeavyItem itemRef) {       // (2)
    itemBody = new int[100000];
    itemID   = ID;
    nextItem = itemRef;
  }
  protected void finalize() throws Throwable {    // (3)
    System.out.println(itemID + ": recycled.");
    super.finalize();
  }
}
//______________________________________________________________________________
public class RecyclingBin {

  public static HeavyItem createHeavyItem(String itemID) {         // (4)
    HeavyItem itemA = new HeavyItem(itemID + " local item", null); // (5)
    itemA = new HeavyItem(itemID, null);                           // (6)
    System.out.println("Return from creating HeavyItem " + itemID);
    return itemA;                                                  // (7)
  }

  public static HeavyItem createList(String listID) {              // (8)
    HeavyItem item3 = new HeavyItem(listID + ": item3", null);     // (9)
    HeavyItem item2 = new HeavyItem(listID + ": item2", item3);    // (10)
    HeavyItem item1 = new HeavyItem(listID + ": item1", item2);    // (11)
    System.out.println("Return from creating list " + listID);
    return item1;                                                  // (12)
  }

  public static void main(String[] args) {                         // (13)
    HeavyItem list = createList("X");                              // (14)
    list = createList("Y");                                        // (15)

    HeavyItem itemOne = createHeavyItem("One");                    // (16)
    HeavyItem itemTwo = createHeavyItem("Two");                    // (17)
    itemOne = null;                                                // (18)
    createHeavyItem("Three");                                      // (19)
    createHeavyItem("Four");                                       // (20)
    System.out.println("Return from main().");
  }
}