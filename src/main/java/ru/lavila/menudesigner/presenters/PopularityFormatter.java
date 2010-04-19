package ru.lavila.menudesigner.presenters;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class PopularityFormatter
{
    public static String format(double popularity)
    {
        NumberFormat numberFormat = DecimalFormat.getInstance();
        numberFormat.setMaximumFractionDigits(10);
        return numberFormat.format(popularity);
    }
}
