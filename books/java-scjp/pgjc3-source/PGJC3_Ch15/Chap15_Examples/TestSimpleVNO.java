
public class TestSimpleVNO {
  public static void main(String[] args) {
    // Three individual version numbers.
    SimpleVNO latest  = new SimpleVNO(9,1,1);                               // (1)
    SimpleVNO inShops = new SimpleVNO(9,1,1);                               // (2)
    SimpleVNO older   = new SimpleVNO(6,6,6);                               // (3)

    // An array of version numbers.
    SimpleVNO[] versions =  new SimpleVNO[] {                               // (4)
        new SimpleVNO( 3,49, 1), new SimpleVNO( 8,19,81),
        new SimpleVNO( 2,48,28), new SimpleVNO(10,23,78),
        new SimpleVNO( 9, 1, 1)};

    // An array with number of downloads.
    Integer[] downloads = {245, 786, 54,1010, 123};                         // (5)

    TestCaseVNO.test(latest, inShops, older, versions, downloads);          // (6)
  }
}