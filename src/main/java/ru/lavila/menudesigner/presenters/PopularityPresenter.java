package ru.lavila.menudesigner.presenters;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;

public class PopularityPresenter
{
    public final double value;
    private static final NumberFormat numberFormat = DecimalFormat.getInstance();

    static
    {
        numberFormat.setMaximumFractionDigits(10);
    }

    public PopularityPresenter(double value)
    {
        this.value = value;
    }

    public PopularityPresenter(String value) throws ParseException
    {
        this.value = numberFormat.parse(value).doubleValue();
    }

    @Override
    public String toString()
    {
        return numberFormat.format(value);
    }
}
