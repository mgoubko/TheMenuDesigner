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
    private final ItemsList itemsList;
    private final HierarchySaver hierarchySaver;
    private final HierarchyCalculator hierarchyCalculator;

    public TargetTreeController(Hierarchy hierarchy, ItemsList itemsList, HierarchyCalculator hierarchyCalculator)
    {
        this.hierarchy = hierarchy;
        this.itemsList = itemsList;
        this.hierarchyCalculator = hierarchyCalculator;
        this.hierarchySaver = new HierarchySaver();
    }

    public void classifyByTaxonomy(Hierarchy taxonomy, Category category)
    {
        new CategoryClassifier(new CategoryManipulator(hierarchy, category)).classify(taxonomy);
    }

    public void optimizeByTaxonomy(Hierarchy taxonomy, Category category)
    {
        CategoryEvaluator evaluator = new TopDownTreeEvaluator(hierarchy, hierarchyCalculator);
//        CategoryEvaluator evaluator = new CostCategoryEvaluator(hierarchyCalculator);
        CategoryManipulator manipulator = new CategoryManipulator(hierarchy, category, evaluator);
        manipulator.apply(new LocalSearchCategoryOptimizer(manipulator, hierarchyCalculator.getMenuModel()).optimize(taxonomy));
    }

    public void sortByPriority(Category category)
    {
        new CategoryManipulator(hierarchy, category).sortByPopularity();
    }

    public void optimize()
    {
        new HierarchyOptimizer(itemsList, hierarchy, hierarchyCalculator).optimize();
    }

    public void save(String filename)
    {
        hierarchySaver.saveHierarchy(filename, hierarchy, itemsList);
    }
}
