class BasicBlob {                                     // (1)
  static    int idCounter;
  static    int population;
  protected int blobId;

  BasicBlob() {
    blobId = idCounter++;
    ++population;
  }
  protected void finalize() throws Throwable {        // (2)
    --population;
    super.finalize();
  }
}

class Blob extends BasicBlob {                        // (3)
  int[] fat;

  Blob(int bloatedness) {                             // (4)
    fat = new int[bloatedness];
  }

  protected void finalize() throws Throwable {        // (5)
    System.out.println(blobId + ": Bye");
    super.finalize();
  }
}