package hu.android.ait.americanabroad.converters;

import java.text.DecimalFormat;

/**
 * Created by andreadean on 12/12/2015.
 */
public class ConvertDistance implements Converter {

    static final double RATE = 1.60943;
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
        return (df.format(Double.valueOf(val)).toString());

    }

    @Override
    public String formatUS(String val) {
        return (df.format(Double.valueOf(val)).toString());

    }

}
