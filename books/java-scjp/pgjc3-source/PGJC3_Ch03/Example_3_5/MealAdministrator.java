// Filename: MealAdministrator.java
public class MealAdministrator {
  public static void main(String[] args) {

    System.out.printf(                                      // (5)
        "Please note that no eggs will be served at %s, %02d:%02d.%n",
        Meal.BREAKFAST, Meal.BREAKFAST.getHour(), Meal.BREAKFAST.getMins()
    );

    System.out.println("Meal times are as follows:");
    Meal[] meals = Meal.values();                           // (6)
    for (Meal meal : meals)                                 // (7)
      System.out.printf("%s served at %02d:%02d%n",
                 meal, meal.getHour(), meal.getMins()
      );

    Meal formalDinner = Meal.valueOf("DINNER");             // (8)
    System.out.printf("Formal dress is required for %s at %02d:%02d.%n",
        formalDinner, formalDinner.getHour(), formalDinner.getMins()
    );
  }
}