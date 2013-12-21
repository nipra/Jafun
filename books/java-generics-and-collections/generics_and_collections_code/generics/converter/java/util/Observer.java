package java.util;

/* stub */

public interface Observer<Oble extends Observable<Oble,Ober,Arg>,
	 	          Ober extends Observer<Oble,Ober,Arg>,
		          Arg>
{
    public void update(Oble o, Arg a);
}
