class BasicBlob {                                              // (1)
  static    int idCounter;
  static    int population;
  protected int blobId;

  BasicBlob() {
    blobId = idCounter++;
    ++population;
  }
  protected void finalize() throws Throwable {                 // (2)
    --population;
    super.finalize();
  }
}
//______________________________________________________________________________
class Blob extends BasicBlob {                                 // (3)
  int[] size;

  Blob(int bloatedness) {                                      // (4)
    size = new int[bloatedness];
    System.out.println(blobId + ": Hello");
  }

  protected void finalize() throws Throwable {                 // (5)
    System.out.println(blobId + ": Bye");
    super.finalize();
  }
}