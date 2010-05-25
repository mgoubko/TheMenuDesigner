package ru.lavila.menudesigner.math;

import ru.lavila.menudesigner.models.*;

import java.util.List;

public class HierarchyCalculator extends MenuModelClient implements MenuModelListener
{
    private final ItemsListCalculator itemsCalculator;
    private final Hierarchy hierarchy;

    public HierarchyCalculator(ItemsListCalculator itemsCalculator, Hierarchy hierarchy)
    {
        this.itemsCalculator = itemsCalculator;
        itemsCalculator.addModelListener(this);
        this.hierarchy = hierarchy;
    }

    public double getHierarchySearchTime()
    {
        return getSearchTimeWithInherited(hierarchy.getRoot());
    }

    public double getOptimalSearchTime()
    {
        return itemsCalculator.getOptimalSearchTime();
    }

    public double getCategoryTimeLoss(Category category)
    {
        double result = getSubHierarchyTimeLoss(category);
        for (Element element : category.getElements())
        {
            if (element instanceof Category)
            {
                result -= getSubHierarchyTimeLoss((Category) element);
            }
        }
        return result;
    }

    private double getSubHierarchyTimeLoss(Category category)
    {
        return getSearchTimeWithInherited(category) - itemsCalculator.getOptimalSearchTime(category.getGroup());
    }

    private double getSearchTimeWithInherited(Category category)
    {
        double result = 0;
        List<Element> elements = category.getElements();
        int totalElements = elements.size();
        for (int index = 0; index < totalElements; index++)
        {
            Element element = elements.get(index);
            result += itemsCalculator.getMenuModel().getTimeToSelect(index + 1, totalElements) * element.getPopularity();
            if (element instanceof Category)
            {
                result += getSearchTimeWithInherited((Category) element);
            }
        }
        return result;
    }

    public void menuModelChanged()
    {
        fireModelChanged();
    }
}
