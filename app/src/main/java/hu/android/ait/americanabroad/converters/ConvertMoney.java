package hu.android.ait.americanabroad.converters;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import java.text.DecimalFormat;
import java.util.Currency;

import hu.android.ait.americanabroad.data.MoneyResult;

/**
 * Created by andreadean on 12/12/2015.
 */
public class ConvertMoney implements Converter {

    double RATE;
    CurrencyHelper currencyHelper;
    static final DecimalFormat df = new DecimalFormat("0.00");


    public ConvertMoney(CurrencyHelper currencyHelper){
        this.currencyHelper = currencyHelper;
        RATE = currencyHelper.getRATE() * 1.0;
    }


    @Override
    public String xToUS(String val) {
        return df.format(Double.valueOf(val)/RATE).toString();

//        return currencyHelper.getFormattedCurrencyString("USD", Double.valueOf(val) / RATE);
    }

    @Override
    public String usToX(String val) {
        return df.format(Double.valueOf(val)*RATE).toString();

//        return currencyHelper.getFormattedCurrencyString(currencyHelper.getCurrency().getCurrencyCode(), Double.valueOf(val) * RATE);
    }

    @Override
    public String formatX(String val) {
        return df.format(Double.valueOf(val)).toString();

//        return currencyHelper.getFormattedCurrencyString(currencyHelper.getCurrency().getCurrencyCode(), Double.valueOf(val));
    }

    @Override
    public String formatUS(String val) {
        return df.format(Double.valueOf(val)).toString();

//        return currencyHelper.getFormattedCurrencyString("USD", Double.valueOf(val));
    }
}
