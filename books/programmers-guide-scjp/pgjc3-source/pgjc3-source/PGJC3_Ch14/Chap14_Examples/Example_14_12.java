import java.util.List;

class SupA {
  public void set(Integer ref) {/*...*/}           // (1)
  public void set(List<Integer> list) {/*...*/}    // (2)
}

class SubA extends SupA {
  @Override
  public void set(Integer iRef) {/*...*/}  // (1') same as at (1)
  @Override
  public void set(List list) {/*...*/}     // (2') same as the erasure at (2)
}