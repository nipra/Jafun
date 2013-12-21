class SupJ {
  public void set(Object obj) {/*...*/}// (1)
  public Object get() {return null;}   // (2)
}

class SubJ extends SupJ {
  public <S> void set(S s) {/*...*/}   // (1a) Error: same erasure
  public <S> S get() {return null;}    // (2a) Error: same erasure
}