package ru.lavila.menudesigner.controllers;

import ru.lavila.menudesigner.io.HierarchySaver;
import ru.lavila.menudesigner.math.HierarchyCalculator;
import ru.lavila.menudesigner.math.ItemsListCalculator;
import ru.lavila.menudesigner.math.classifiers.*;
import ru.lavila.menudesigner.models.Category;
import ru.lavila.menudesigner.models.Hierarchy;
import ru.lavila.menudesigner.models.ItemsList;

public class TargetTreeController
{
    private final Hierarchy hierarchy;
    private final ItemsList itemsList;
    private final HierarchySaver hierarchySaver;
    private final ItemsListCalculator calculator;
    private final HierarchyCalculator hierarchyCalculator;

    public TargetTreeController(Hierarchy hierarchy, ItemsList itemsList, ItemsListCalculator calculator, HierarchyCalculator hierarchyCalculator)
    {
        this.hierarchy = hierarchy;
        this.itemsList = itemsList;
        this.calculator = calculator;
        this.hierarchyCalculator = hierarchyCalculator;
        this.hierarchySaver = new HierarchySaver();
    }

    public void classifyByTaxonomy(Hierarchy taxonomy, Category category)
    {
        new CategoryClassifier(hierarchy, category).classify(taxonomy);
    }

    public void optimizeByTaxonomy(Hierarchy taxonomy, Category category)
    {
//        new GreedyCategoryOptimizer(hierarchy, category).optimize(taxonomy, calculator.getMenuModel());
//        new LocalSearchCategoryOptimizer(hierarchy, category, new CostCategoryEvaluator(calculator)).optimize(taxonomy);
        CategoryManipulator manipulator = new CategoryManipulator(hierarchy, category, new TopDownTreeEvaluator(calculator, hierarchy, hierarchyCalculator));
        manipulator.apply(new LocalSearchCategoryOptimizer(manipulator).optimize(taxonomy));
    }

    public void sortByPriority(Category category)
    {
        new CategoryClassifier(hierarchy, category).sortByPopularity();
    }

    public void optimize()
    {
        new HierarchyOptimizer(hierarchy, new TopDownTreeEvaluator(calculator, hierarchy, hierarchyCalculator), itemsList).optimize();
    }

    public void save(String filename)
    {
        hierarchySaver.saveHierarchy(filename, hierarchy, itemsList);
    }
}
