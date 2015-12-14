package hu.android.ait.americanabroad.converters;

import android.util.Log;

import java.text.DecimalFormat;

import hu.android.ait.americanabroad.R;

/**
 * Created by andreadean on 12/12/2015.
 */
public class ConvertTemperature implements Converter {

    static final DecimalFormat df = new DecimalFormat("0.0#");


    @Override
    public String xToUS(String val) {
        double f = ((9.0/5.0)*(Double.valueOf(val)) + 32);
        Log.d("LOGLOG", "2x " + val);
        Log.d("LOGLOG", "2f " + f);
        return df.format(f);
    }

    @Override
    public String usToX(String val) {
        double c = (5.0/9.0)*(Double.valueOf(val) - 32);
        Log.d("LOGLOG", "1f " + val);
        Log.d("LOGLOG", "1c " + c);
        return df.format(c);
    }

    @Override
    public String formatX(String val) {
        return df.format(Double.valueOf(val)).toString();

    }

    @Override
    public String formatUS(String val) {
        return df.format(Double.valueOf(val)).toString();

    }

}
