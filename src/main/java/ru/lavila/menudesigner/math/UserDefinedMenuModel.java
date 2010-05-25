package ru.lavila.menudesigner.math;

public class UserDefinedMenuModel implements MenuModel
{
    private final String name;
    private final double[][] values;

    public UserDefinedMenuModel(String name, double[][] values)
    {
        this.name = name;
        this.values = values;
    }

    public String getName()
    {
        return name;
    }

    public double getTimeToSelect(int target, int total)
    {
        if (total > values.length || target > values[total].length) return Double.MAX_VALUE;
        return values[total - 1][target - 1];
    }

    public double[] getOptimalProportion(int itemsSize)
    {
        return new double[]{0.25, 0.25, 0.25, 0.25};
    }
}
