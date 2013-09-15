public interface MyActionListener<E extends Exception> {
  public void doAction() throws E;       // Type parameter in throws clause
}