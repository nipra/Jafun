// Filename: MealPrices.java
public class MealPrices {

  public static void main(String[] args) {                               // (8)
    System.out.printf(
        "Please note that %s, %02d:%02d, on %s costs $%.2f.%n",
        Meal.BREAKFAST.name(),                                           // (9)
        Meal.BREAKFAST.getHour(), Meal.BREAKFAST.getMins(),
        Day.MONDAY,
        Meal.BREAKFAST.mealPrice(Day.MONDAY)                             // (10)
    );

    System.out.println("Meal prices on " + Day.SATURDAY + " are as follows:");
    Meal[] meals = Meal.values();
    for (Meal meal : meals)
      System.out.printf(
          "%s costs $%.2f.%n", meal, meal.mealPrice(Day.SATURDAY)        // (11)
      );
  }
}