import java.io.Serializable;

public class SerializableSingleton implements Serializable {

  public final static SerializableSingleton INSTANCE 
   = new SerializableSingleton();
  
  private SerializableSingleton() {}
  
}
