package collections;

import java.util.*;
import static collections.StaticTestData.*;

public class CollectionMethodsTest {

  public static void main(String[] args) {
    mondayTasks.add(new PhoneTask("Ruth", "567 1234"));
    assert mondayTasks.toString().equals(
            "[code logic, phone Mike, phone Ruth]");
    Collection<Task> allTasks = new ArrayList<Task>(mondayTasks);
    allTasks.addAll(tuesdayTasks);
    assert allTasks.toString().equals(
            "[code logic, phone Mike, phone Ruth, code db, code gui, phone Paul]");
    boolean wasPresent = mondayTasks.remove(mikePhone);
    assert wasPresent;
    assert mondayTasks.toString().equals("[code logic, phone Ruth]");
    mondayTasks.clear();
    assert mondayTasks.toString().equals("[]");
    Collection<Task> tuesdayNonphoneTasks = new ArrayList<Task>(tuesdayTasks);
    tuesdayNonphoneTasks.removeAll(phoneTasks);
    assert tuesdayNonphoneTasks.toString().equals("[code db, code gui]");
    Collection<Task> phoneTuesdayTasks = new ArrayList<Task>(tuesdayTasks);
    phoneTuesdayTasks.retainAll(phoneTasks);
    assert phoneTuesdayTasks.toString().equals("[phone Paul]");
    Collection<PhoneTask> tuesdayPhoneTasks =
            new ArrayList<PhoneTask>(phoneTasks);
    tuesdayPhoneTasks.retainAll(tuesdayTasks);
    assert tuesdayPhoneTasks.toString().equals("[phone Paul]");
    assert tuesdayPhoneTasks.contains(paulPhone);
    assert tuesdayTasks.containsAll(tuesdayPhoneTasks);
    assert mondayTasks.isEmpty();
    assert mondayTasks.size() == 0;
// throws ConcurrentModificationException
    for (Task t : tuesdayTasks) {
      if (t instanceof PhoneTask) {
        tuesdayTasks.remove(t);
      }
    }
// throws ConcurrentModificationException
    for (Iterator<Task> it = tuesdayTasks.iterator() ; it.hasNext() ; ) {
      Task t = it.next();
      if (t instanceof PhoneTask) {
        tuesdayTasks.remove(t);
      }
    }
    for (Iterator<Task> it = tuesdayTasks.iterator() ; it.hasNext() ; ) {
      Task t = it.next();
      if (t instanceof PhoneTask) {
        it.remove();
      }
    }
  }
}