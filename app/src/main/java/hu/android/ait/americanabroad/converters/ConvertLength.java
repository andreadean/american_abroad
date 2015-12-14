package hu.android.ait.americanabroad.converters;

import java.text.DecimalFormat;

import hu.android.ait.americanabroad.MainActivity;

/**
 * Created by andreadean on 12/12/2015.
 */
public class ConvertLength implements Converter {

    static final double RATE = 2.54;
    static final DecimalFormat df = new DecimalFormat("0.0#");

    @Override
    public String xToUS(String val) {
        return df.format(Double.valueOf(val) / RATE).toString();

    }

    @Override
    public String usToX(String val) {
        return df.format(Double.valueOf(val) * RATE).toString();

    }

    @Override
    public String formatX(String val) {
        return df.format(Double.valueOf(val)).toString() + " in";

    }

    @Override
    public String formatUS(String val) {
        return df.format(Double.valueOf(val)).toString() + " in";
    }

    public String format(String val){
        return df.format(Double.valueOf(val)).toString();
    }


}
