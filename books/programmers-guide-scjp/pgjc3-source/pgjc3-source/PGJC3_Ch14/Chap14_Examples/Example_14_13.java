class SupB<T> {
  public void set(T t) {/*...*/}       // (1)
  public T get() {return null;}        // (2)
}

class SubB1 extends SupB<Number> {
  @Override
  public void set(Number num) {/*...*/} // (1a) Overrides
  @Override
  public Number get() {return 0;}       // (2a) Overrides
}

class SubB2 extends SupB<Number> {
  public void set(Object obj) {/*...*/} // (1b) Error: same erasure
  public void set(Long l) {/*...*/}     // (1c) Overloads

  public Object get() {return 0;}       // (2b) Error: incompatible return type
}