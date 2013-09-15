import java.util.Locale;
public class LocalesEverywhere {

  public static void main(String[] args) {

    Locale locDF = Locale.getDefault();
    Locale locNO =  new Locale("no", "NO");     // Locale: Norwegian/Norway
    Locale locFR =  new Locale("fr", "FR");     // Locale: French/France 

    // Display country name for Norwegian locale:
    System.out.println("In " + locDF.getDisplayCountry() + "(default)" +
                        ": " + locNO.getDisplayCountry());
    System.out.println("In " + locNO.getDisplayCountry() +
                        ": " + locNO.getDisplayCountry(locNO));
    System.out.println("In " + locFR.getDisplayCountry() +
                        ": " + locNO.getDisplayCountry(locFR));

    // Display language name for Norwegian locale:
    System.out.println("In " + locDF.getDisplayCountry() +  "(default)" +
                        ": " + locNO.getDisplayLanguage());
    System.out.println("In " + locNO.getDisplayCountry() +
                        ": " + locNO.getDisplayLanguage(locNO));
    System.out.println("In " + locFR.getDisplayCountry() +
                        ": " + locNO.getDisplayLanguage(locFR));
  }
}