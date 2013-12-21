import java.io.FileNotFoundException;

public class FileAction implements MyActionListener<FileNotFoundException> {

  public void doAction() throws FileNotFoundException {
     throw new FileNotFoundException("Does not exist");
  }
  public static void main(String[] args) {
    FileAction fileAction = new FileAction();
    try {
      fileAction.doAction();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }
}