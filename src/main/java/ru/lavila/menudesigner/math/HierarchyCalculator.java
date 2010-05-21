package ru.lavila.menudesigner.math;

import ru.lavila.menudesigner.models.*;

import java.util.Arrays;
import java.util.List;

public class HierarchyCalculator
{
    private final ItemsList itemsList;
    private final Hierarchy hierarchy;
    private final MenuModel menuModel;

    public HierarchyCalculator(ItemsList itemsList, Hierarchy hierarchy)
    {
        this.itemsList = itemsList;
        this.hierarchy = hierarchy;
        this.menuModel = new ReadUntilWithErrorMenuModel(1, 0, 1, 0.5, 0.05);
    }

    public double getHierarchySearchTime()
    {
        return getSearchTimeWithInherited(hierarchy.getRoot());
    }

    public double getOptimalSearchTime()
    {
        return getOptimalSearchTime(Arrays.asList(itemsList.toArray()));
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
        return getSearchTimeWithInherited(category) - getOptimalSearchTime(category.getGroup());
    }

    private double getSearchTimeWithInherited(Category category)
    {
        double result = 0;
        List<Element> elements = category.getElements();
        int totalElements = elements.size();
        for (int index = 0; index < totalElements; index++)
        {
            Element element = elements.get(index);
            result += menuModel.getTimeToSelect(index + 1, totalElements) * element.getPopularity();
            if (element instanceof Category)
            {
                result += getSearchTimeWithInherited((Category) element);
            }
        }
        return result;
    }

    private double getSearchTime(double[] proportion)
    {
        double result = 0;
        for (int index = 0; index < proportion.length; index++)
        {
            result += menuModel.getTimeToSelect(index + 1, proportion.length) * proportion[index];
        }
        return result;
    }

    private double getOptimalSearchTime(List<Item> items)
    {
        double[] proportion = menuModel.getOptimalProportion();
        double result = getSearchTime(proportion);
        double sum = 0;
        for (double popularity : proportion)
        {
            sum += popularity * Math.log(popularity);
        }
        result /= sum;
        sum = 0;
        for (Item item : items)
        {
            sum += item.getPopularity() * Math.log(item.getPopularity());
        }
        return result * sum;
    }

}
