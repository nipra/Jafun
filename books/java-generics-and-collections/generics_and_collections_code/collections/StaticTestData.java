package collections;

import java.util.*;

public class StaticTestData {
  public static PhoneTask mikePhone = new PhoneTask("Mike", "987 6543");
  public static PhoneTask paulPhone = new PhoneTask("Paul", "123 4567");
  public static CodingTask databaseCode = new CodingTask("db");
  public static CodingTask interfaceCode = new CodingTask("gui");
  public static CodingTask logicCode = new CodingTask("logic");
  public static Collection<PhoneTask> phoneTasks = new ArrayList<PhoneTask>();
  public static Collection<CodingTask> codingTasks = new ArrayList<CodingTask>();
  public static Collection<Task> mondayTasks = new ArrayList<Task>();
  public static Collection<Task> tuesdayTasks = new ArrayList<Task>();
  static{
    Collections.addAll(phoneTasks, mikePhone, paulPhone);
    Collections.addAll(codingTasks, databaseCode, interfaceCode, logicCode);
    Collections.addAll(mondayTasks, logicCode, mikePhone);
    Collections.addAll(tuesdayTasks, databaseCode, interfaceCode, paulPhone);
    assert phoneTasks.toString().equals("[phone Mike, phone Paul]");
    assert codingTasks.toString().equals("[code db, code gui, code logic]");
    assert mondayTasks.toString().equals("[code logic, phone Mike]");
    assert tuesdayTasks.toString().equals("[code db, code gui, phone Paul]");
  }
}