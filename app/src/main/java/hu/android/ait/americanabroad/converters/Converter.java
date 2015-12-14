package hu.android.ait.americanabroad.converters;

/**
 * Created by andreadean on 12/13/2015.
 */
public interface Converter {

    public String xToUS(String val);

    public String usToX(String val);

    public String formatX(String val);

    public String formatUS(String val);

}
