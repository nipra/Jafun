class LymphNode implements IMonoLink<Lymph> {
  private Lymph            body;
  private IMonoLink<Lymph> location;
  public  void  setData(Lymph obj) { body = obj; }
  public  Lymph getData()          { return body; }
  public  void  setNext(IMonoLink<Lymph> loc) { this.location = loc; }
  public  IMonoLink<Lymph> getNext()          { return this.location; }
}
class Lymph { /*... */ }