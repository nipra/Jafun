
public class ThreadStates {

  private static Thread t1 = new Thread("T1") {    // (1)
    public void run() {
      try {
        sleep(2);                                  // (2)
        for(int i = 10000; i > 0; i--);            // (3)
      } catch (InterruptedException ie){
        ie.printStackTrace();
      }
    }
  };

  public static void main(String[] args) {
    t1.start();
    while(true) {                                  // (4)
      Thread.State state = t1.getState();
      System.out.println(state);
      if (state == Thread.State.TERMINATED) break;
    }
  }
}