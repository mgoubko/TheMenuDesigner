package ru.lavila.menudesigner.controllers;

import ru.lavila.menudesigner.math.CategoryClassifier;
import ru.lavila.menudesigner.math.ItemsListCalculator;
import ru.lavila.menudesigner.models.Category;
import ru.lavila.menudesigner.models.Element;
import ru.lavila.menudesigner.models.Hierarchy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TargetTreeController
{
    private final Hierarchy hierarchy;
    private final ItemsListCalculator calculator;

    public TargetTreeController(Hierarchy hierarchy, ItemsListCalculator calculator)
    {
        this.hierarchy = hierarchy;
        this.calculator = calculator;
    }

    public void classifyByTaxonomy(Hierarchy taxonomy, Category category)
    {
        new CategoryClassifier(hierarchy, category).classify(taxonomy);
    }

    public void optimizeByTaxonomy(Hierarchy taxonomy, Category category)
    {
        new CategoryClassifier(hierarchy, category).optimize(taxonomy, calculator.getMenuModel());
    }

    public void sortByPriority(Category category)
    {
        List<Element> elements = new ArrayList<Element>(category.getElements());
        Collections.sort(elements, new Comparator<Element>()
        {
            public int compare(Element element1, Element element2)
            {
                return Double.compare(element2.getPopularity(), element1.getPopularity());
            }
        });
        hierarchy.add(category, elements.toArray(new Element[elements.size()]));
    }
}
