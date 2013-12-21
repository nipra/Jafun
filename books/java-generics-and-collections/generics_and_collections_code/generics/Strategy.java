abstract class TaxPayer<P extends TaxPayer<P>> {
  public long income;  // in cents
  private TaxStrategy<P> strategy;
  public TaxPayer(long income, TaxStrategy<P> strategy) {
    this.income = income; this.strategy = strategy;
  }
  protected abstract P getThis();
  public long getIncome() { return income; }
  public long computeTax() { return strategy.computeTax(getThis()); }
}

interface TaxStrategy<P extends TaxPayer<P>> {
  public long computeTax(P p);
}

class DefaultTaxStrategy<P extends TaxPayer<P>> implements TaxStrategy<P> {
  private static final double RATE = 0.40;
  public long computeTax(P payer) {
    return Math.round(payer.getIncome() * RATE);
  }
}

class DodgingTaxStrategy<P extends TaxPayer<P>> implements TaxStrategy<P> {
  public long computeTax(P payer) { return 0; }
}

class Person extends TaxPayer<Person> {
  public Person(long income, TaxStrategy<Person> strategy) {
    super(income, strategy);
  }
  protected Person getThis() { return this; }
}

class Trust extends TaxPayer<Trust> {
  private boolean nonprofit;
  public Trust(long income, boolean nonprofit, TaxStrategy<Trust> strategy) {
    super(income, strategy); this.nonprofit = nonprofit;
  }
  protected Trust getThis() { return this; }
  public boolean isNonprofit() { return nonprofit; }
}

class TrustTaxStrategy extends DefaultTaxStrategy<Trust> {
  public long computeTax(Trust trust) {
    return trust.isNonprofit() ? 0 : super.computeTax(trust);
  }
}

class Test {
  public static void main(String[] args) {

    Person person, dodger;
    person = new Person(10000000, new DefaultTaxStrategy<Person>());
    dodger = new Person(10000000, new DodgingTaxStrategy<Person>());
    assert person.computeTax() == 4000000;
    assert dodger.computeTax() == 0;

    Trust forProfit, nonProfit;
    forProfit = new Trust(10000000, false, new TrustTaxStrategy());
    nonProfit = new Trust(10000000, true, new TrustTaxStrategy());
    assert forProfit.computeTax() == 4000000;
    assert nonProfit.computeTax() == 0;
  }
}

