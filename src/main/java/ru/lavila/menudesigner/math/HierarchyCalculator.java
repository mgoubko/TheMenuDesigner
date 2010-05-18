package ru.lavila.menudesigner.math;

import ru.lavila.menudesigner.models.Category;
import ru.lavila.menudesigner.models.Element;
import ru.lavila.menudesigner.models.Hierarchy;

import java.util.List;

public class HierarchyCalculator
{
    private final Hierarchy hierarchy;
    private final MenuModel menuModel;

    public HierarchyCalculator(Hierarchy hierarchy)
    {
        this.hierarchy = hierarchy;
        this.menuModel = new ReadUntilMenuModel(1, 0, 1, 0.5);
    }

    public double getUserSessionTime()
    {
        return getAverageSearchTime(hierarchy.getRoot());
    }

    private double getAverageSearchTime(Category category)
    {
        double result = 0;
        List<Element> elements = category.getElements();
        int totalElements = elements.size();
        for (int index = 0; index < totalElements; index++)
        {
            Element element = elements.get(index);
            result += menuModel.getTimeToSelect(index, totalElements) * element.getPopularity();
            if (element instanceof Category)
            {
                result += getAverageSearchTime((Category) element);
            }
        }
        return result;
    }
}
