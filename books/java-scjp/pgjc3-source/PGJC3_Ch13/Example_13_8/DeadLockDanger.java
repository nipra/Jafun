
public class DeadLockDanger {

  String o1 = "Lock " ;                        // (1)
  String o2 = "Step ";                         // (2)

  Thread t1 = (new Thread("Printer1") {        // (3)
    public void run() {
      while(true) {
        synchronized(o1) {                     // (4)
          synchronized(o2) {                   // (5)
            System.out.println(o1 + o2);
          }
        }
      }
    }
  });

  Thread t2 = (new Thread("Printer2") {        // (6)
    public void run() {
      while(true) {
        synchronized(o2) {                     // (7)
          synchronized(o1) {                   // (8)
            System.out.println(o2 + o1);
          }
        }
      }
    }
  });

  public static void main(String[] args) {
    DeadLockDanger dld = new DeadLockDanger();
    dld.t1.start();
    dld.t2.start();
  }
}