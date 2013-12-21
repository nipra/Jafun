//______________________________________________________________________________
public class MemoryCheck {
  public static void main(String[] args) {                           // (6)
    int blobsRequired, blobSize;
    try {
      blobsRequired = Integer.parseInt(args[0]);
      blobSize      = Integer.parseInt(args[1]);
    } catch(IndexOutOfBoundsException e) {
      System.err.println("Usage: MemoryCheck <number of blobs> <blob size>");
      return;
    }
    Runtime environment = Runtime.getRuntime();                      // (7)
    System.out.println("Total memory: " + environment.totalMemory());// (8)
    System.out.println("Free memory before blob creation: " +
                       environment.freeMemory());                    // (9)
    for (int i=0; i<blobsRequired; ++i) {                            // (10)
      new Blob(blobSize);
    }
    System.out.println("Free memory after blob creation: " +
                       environment.freeMemory());                    // (11)
    System.gc();                                                     // (12)
    System.out.println("Free memory after requesting GC: " +
                       environment.freeMemory());                    // (13)
    System.out.println(BasicBlob.population + " blobs alive");       // (14)
  }
}