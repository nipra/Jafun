import java.awt.Component;
import javax.swing.ProgressMonitor;
import java.beans.*;
import java.util.jar.Pack200;

public class PackProgressMonitor extends ProgressMonitor
 implements PropertyChangeListener {

  public PackProgressMonitor(Component parent) {
    super(parent, null, "Packing", -1, 100);
  }

  public void propertyChange(PropertyChangeEvent event) {
    if (event.getPropertyName().equals(Pack200.Packer.PROGRESS)) {
      String newValue = (String) event.getNewValue();
      int value = Integer.parseInt(newValue);
      this.setProgress(value);
    }
  }
}
