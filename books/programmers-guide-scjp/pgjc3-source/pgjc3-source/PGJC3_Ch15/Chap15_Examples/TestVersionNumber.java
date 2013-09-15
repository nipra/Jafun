
public class TestVersionNumber {
  public static void main(String[] args) {
    // Three individual version numbers.
    VersionNumber latest  = new VersionNumber(9,1,1);                       // (1)
    VersionNumber inShops = new VersionNumber(9,1,1);                       // (2)
    VersionNumber older   = new VersionNumber(6,6,6);                       // (3)

    // An array of version numbers.
    VersionNumber[] versions =  new VersionNumber[] {                       // (4)
        new VersionNumber( 3,49, 1), new VersionNumber( 8,19,81),
        new VersionNumber( 2,48,28), new VersionNumber(10,23,78),
        new VersionNumber( 9, 1, 1)};

    // An array with number of downloads.
    Integer[] downloads = {245, 786, 54,1010, 123};                         // (5)

    TestCaseVNO.test(latest, inShops, older, versions, downloads);          // (6)
  }
}