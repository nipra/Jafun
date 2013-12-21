package java.util;

/* stub */
class StubException extends UnsupportedOperationException {}

public class Observable<Oble extends Observable<Oble,Ober,Arg>,
	 	        Ober extends Observer<Oble,Ober,Arg>,
		        Arg>
{
    public void addObserver(Ober o)    { throw new StubException(); }
    protected void clearChanged()      { throw new StubException(); }
    public int countObservers()        { throw new StubException(); }
    public void deleteObserver(Ober o) { throw new StubException(); }
    public boolean hasChanged()        { throw new StubException(); }
    public void notifyObservers()      { throw new StubException(); }
    public void notifyObservers(Arg a) { throw new StubException(); }
    protected void setChanged()        { throw new StubException(); }
}
