package ru.lavila.menudesigner.controllers;

import ru.lavila.menudesigner.io.HierarchySaver;
import ru.lavila.menudesigner.math.HierarchyCalculator;
import ru.lavila.menudesigner.math.ItemsListCalculator;
import ru.lavila.menudesigner.math.classifiers.*;
import ru.lavila.menudesigner.models.Category;
import ru.lavila.menudesigner.models.Hierarchy;
import ru.lavila.menudesigner.models.ItemsList;

public class TargetTreeController {
    private final Hierarchy hierarchy;
    private final ItemsList itemsList;
    private final HierarchySaver hierarchySaver;
    private final HierarchyOptimizer hierarchyOptimizer;

    public TargetTreeController(Hierarchy hierarchy, ItemsList itemsList, HierarchyCalculator hierarchyCalculator, ItemsListCalculator itemsListCalculator) {
        this.hierarchy = hierarchy;
        this.itemsList = itemsList;
        this.hierarchySaver = new HierarchySaver();
        this.hierarchyOptimizer = new HierarchyOptimizer(itemsList, hierarchy, hierarchyCalculator, itemsListCalculator);
    }

    public void classifyByTaxonomy(Hierarchy taxonomy, Category category) {
        new CategoryClassifier(new CategoryManipulator(hierarchy, category)).classify(taxonomy);
    }

    public void optimizeByTaxonomy(Category category, Hierarchy taxonomy) {
        hierarchyOptimizer.optimizeByTaxonomy(category, taxonomy);
    }

    public void sortByPriority(Category category) {
        new CategoryManipulator(hierarchy, category).sortByPopularity();
    }

    public void optimizeSubTree() {
        optimizeSubTree(hierarchy.getRoot());
    }

    public void optimizeSubTree(Category category) {
        hierarchyOptimizer.optimizeSubTree(category);
    }

    public void save(String filename) {
        hierarchySaver.saveHierarchy(filename, hierarchy, itemsList);
    }
}
