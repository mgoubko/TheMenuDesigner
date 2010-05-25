package ru.lavila.menudesigner.math;

public interface MenuModel
{
    /**
     * Returns name to identify model to the user
     * @return model name
     */
    public String getName();

    /**
     * Calculates time to select <code>target</code> element from the list of <code>total</code> elements
     * @param target element to calculate selection time for
     * @param total number of elements in current menu panel
     * @return average time to select element
     */
    public double getTimeToSelect(int target, int total);

    /**
     * Calculates optimal branching factor (size of proportion) and proportion for populartities
     * @param itemsSize total number of target items
     * @return optimal proportion for popularities
     */
    public double[] getOptimalProportion(int itemsSize);
}
