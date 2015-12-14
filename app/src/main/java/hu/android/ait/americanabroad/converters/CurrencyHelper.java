package hu.android.ait.americanabroad.converters;

import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

/**
 * Created by andreadean on 12/5/2015.
 */
public class CurrencyHelper {

    /* From USD to X currency */
    private static double RATE;
    private static Locale MY_LOCALE;
    private Currency currency;

    public CurrencyHelper(Locale locale, double rate) {
        MY_LOCALE = locale;
        RATE = rate;
        currency = Currency.getInstance(MY_LOCALE);
    }

    public CurrencyHelper(Locale locale) {
        MY_LOCALE = locale;
        RATE = -1;
        currency = Currency.getInstance(MY_LOCALE);
    }

    public Currency getCurrency(){
        return currency;
    }

    public String fromXToHome(double xCurrencyAmnt){
         return getFormattedCurrencyString(currency.getCurrencyCode(), xCurrencyAmnt / RATE);
    }

    public String fromHomeToX(double usdCurrencyAmnt){
        return getFormattedCurrencyString(currency.getCurrencyCode(), usdCurrencyAmnt * RATE);
    }

    public static double getRATE() {
        return RATE;
    }

    public static void setRATE(double RATE) {
        CurrencyHelper.RATE = RATE;
    }

    public static Locale getMyLocale() {
        return MY_LOCALE;
    }

    public static void setMyLocale(Locale myLocale) {
        MY_LOCALE = myLocale;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    /*Class made by looking at Adam Speakman's code (http://speakman.net.nz)*/
    public static String getFormattedCurrencyString(String isoCurrencyCode, double amount) {
        // This formats currency values as the user expects to read them (default locale).
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();

        // This specifies the actual currency that the value is in, and provides the currency symbol.
        Currency currency = Currency.getInstance(isoCurrencyCode);

        // Use the US locale as default for the symbol, unless the currency is USD
        // and the locale is NOT the US, in which case we know it should be US$.
        String symbol;
        if (isoCurrencyCode.equalsIgnoreCase("usd") && !Locale.getDefault().equals(Locale.US)) {
            symbol = "US$";
        } else {
            symbol = currency.getSymbol(Locale.US); // US locale has the best symbol formatting table.
        }

        // We then tell our formatter to use this symbol.
        DecimalFormatSymbols decimalFormatSymbols = ((java.text.DecimalFormat) currencyFormat).getDecimalFormatSymbols();
        decimalFormatSymbols.setCurrencySymbol(symbol);
        ((java.text.DecimalFormat) currencyFormat).setDecimalFormatSymbols(decimalFormatSymbols);

        return currencyFormat.format(amount);
    }
}
