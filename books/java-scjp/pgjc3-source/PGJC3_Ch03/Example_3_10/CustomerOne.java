
public class CustomerOne {
  public static void main (String[] args) {
    PizzaFactory pizzaHouse = new PizzaFactory();
    int pricePrPizza = 15;
    double totPrice = pizzaHouse.calcPrice(4, pricePrPizza);       // (1)
    System.out.println("Value of pricePrPizza: " + pricePrPizza);  // Unchanged.
  }
}

class PizzaFactory {
  public double calcPrice(int numberOfPizzas, double pizzaPrice) {  // (2)
    pizzaPrice = pizzaPrice/2.0;       // Changes price.
    return numberOfPizzas * pizzaPrice;
  }
}