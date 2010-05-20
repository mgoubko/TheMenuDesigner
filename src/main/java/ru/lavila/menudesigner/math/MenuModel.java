package ru.lavila.menudesigner.math;

public interface MenuModel
{
    /**
     * Calculates time to select <code>target</code> element from the list of <code>total</code> elements
     * @param target element to calculate selection time for
     * @param total number of elements in current menu panel
     * @return average time to select element
     */
    public double getTimeToSelect(int target, int total);

    public double[] getOptimalProportion();
}
