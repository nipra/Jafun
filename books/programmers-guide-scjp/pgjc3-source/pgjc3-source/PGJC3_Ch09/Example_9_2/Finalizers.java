
public class Finalizers {
  public static void main(String[] args) {                     // (6)
    int blobsRequired, blobSize;
    try {
      blobsRequired = Integer.parseInt(args[0]);
      blobSize      = Integer.parseInt(args[1]);
    } catch(IndexOutOfBoundsException e) {
      System.err.println("Usage: Finalizers <number of blobs> <blob size>");
      return;
    }
    for (int i=0; i<blobsRequired; ++i) {                      // (7)
      new Blob(blobSize);
    }
    System.out.println(BasicBlob.population + " blobs alive"); // (8)
  }
}