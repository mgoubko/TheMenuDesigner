package ru.lavila.menudesigner.controllers;

import ru.lavila.menudesigner.io.HierarchySaver;
import ru.lavila.menudesigner.math.HierarchyCalculator;
import ru.lavila.menudesigner.math.classifiers.*;
import ru.lavila.menudesigner.models.Category;
import ru.lavila.menudesigner.models.Hierarchy;
import ru.lavila.menudesigner.models.ItemsList;

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
//        new GreedyCategoryOptimizer(hierarchy, category).optimize(taxonomy, calculator.getMenuModel());
        new LocalSearchCategoryOptimizer(hierarchy, category, new CostCategoryEvaluator(calculator)).optimize(taxonomy);
    }

    public void sortByPriority(Category category)
    {
        new CategoryClassifier(hierarchy, category).sortByPopularity();
    }

    public void optimize()
    {
        new HierarchyOptimizer(hierarchy, calculator.getMenuModel(), new CostCategoryEvaluator(calculator), itemsList).optimize();
    }

    public void save(String filename)
    {
        hierarchySaver.saveHierarchy(filename, hierarchy, itemsList);
    }
}
