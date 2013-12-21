
public class MyFactoryII implements mypkg.IMachineState {
  public static void main(String[] args) {
    int[] states = { IDLE, BUSY, IDLE, BLOCKED };
    for (int s : states)
      System.out.println(s);
  }
}