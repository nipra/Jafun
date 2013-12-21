// Filename: MachineClient.java
public class MachineClient {
  public static void main(String[] args) {

    Machine machine = new Machine();
    machine.setState(MachineState.IDLE);          // (1) Passed as a value.
    // machine.setState(1);                       // (2) Compile-time error!

    MachineState state = machine.getState();      // (3) Declaring a reference.
    System.out.println(
        "The machine state is: " + state          // (4) Printing the enum name.
    );
    // MachineState newState = new MachineState();// (5) Compile-time error!
  }
}