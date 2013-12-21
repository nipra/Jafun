
public class TestReliableVNO {
  public static void main(String[] args) {
    // Three individual version numbers.
    ReliableVNO latest  = new ReliableVNO(9,1,1);                           // (1)
    ReliableVNO inShops = new ReliableVNO(9,1,1);                           // (2)
    ReliableVNO older   = new ReliableVNO(6,6,6);                           // (3)

    // An array of version numbers.
    ReliableVNO[] versions =  new ReliableVNO[] {                           // (4)
        new ReliableVNO( 3,49, 1), new ReliableVNO( 8,19,81),
        new ReliableVNO( 2,48,28), new ReliableVNO(10,23,78),
        new ReliableVNO( 9, 1, 1)};

    // An array with number of downloads.
    Integer[] downloads = {245, 786, 54,1010, 123};                         // (5)

    TestCaseVNO.test(latest, inShops, older, versions, downloads);          // (6)
  }
}