package ru.lavila.menudesigner.controllers;

import ru.lavila.menudesigner.io.HierarchySaver;
import ru.lavila.menudesigner.math.CategoryClassifier;
import ru.lavila.menudesigner.math.HierarchyCalculator;
import ru.lavila.menudesigner.math.HierarchyOptimizer;
import ru.lavila.menudesigner.models.Category;
import ru.lavila.menudesigner.models.Element;
import ru.lavila.menudesigner.models.Hierarchy;
import ru.lavila.menudesigner.models.ItemsList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TargetTreeController
{
    private final Hierarchy hierarchy;
    private final HierarchyCalculator calculator;
    private final ItemsList itemsList;
    private final HierarchySaver hierarchySaver;


    public TargetTreeController(Hierarchy hierarchy, HierarchyCalculator calculator, ItemsList itemsList)
    {
        this.hierarchy = hierarchy;
        this.calculator = calculator;
        this.itemsList = itemsList;
        this.hierarchySaver = new HierarchySaver();
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

    public void optimize()
    {
        new HierarchyOptimizer(hierarchy, calculator, itemsList).optimize();
    }

    public void save(String filename)
    {
        hierarchySaver.saveHierarchy(filename, hierarchy, itemsList);
    }
}
