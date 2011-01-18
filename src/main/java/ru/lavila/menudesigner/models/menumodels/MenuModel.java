package ru.lavila.menudesigner.models.menumodels;

import ru.lavila.menudesigner.models.Category;
import ru.lavila.menudesigner.models.Element;
import ru.lavila.menudesigner.models.ItemsList;

import java.util.Comparator;

public interface MenuModel
{
    /**
     * Returns name to identify model to the user
     * @return model name
     */
    public String getName();

    /**
     * Initializes menu model to calculate data for specified items list
     * @param itemsList source data to use for further calculations
     */
    public void init(ItemsList itemsList);

    /**
     * Calculates time to select <code>target</code> element from the list of <code>total</code> elements
     * @param target element to calculate selection time for
     * @param total number of elements in current menu panel
     * @return lower bound time to select element
     */
    public double getTimeToSelect(int target, int total);

    /**
     * Calculates time to select element from category
     * @param element element to be selected
     * @param category category with the element
     * @return lower bound of time to select element
     */
    public double getTimeToSelect(Element element, Category category);

    /**
     * Calculates optimal branching factor (size of proportion) and proportion for populartities
     * @return optimal proportion for popularities
     */
    public double[] getOptimalProportion();

    /**
     * Comparator to sort elements in natural order defined by menu model
     * @return elements' comparator
     */
    public Comparator<Element> getElementsComparator();
    public Comparator<Element> getElementsComparator(PopularityCalculator popularityCalculator);

    public interface PopularityCalculator {
        public double getPopularity(Element element);
    }
}
