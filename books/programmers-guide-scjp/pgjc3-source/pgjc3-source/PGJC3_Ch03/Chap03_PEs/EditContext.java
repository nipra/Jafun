/** A JavaBean for an editing context */
public class EditContext {             // Non-generic version
  private Object selected;
  public void setSelected(Object newSelected) {
    selected = newSelected;
  }
  public Object getSelected() {
    return selected;
  }
}