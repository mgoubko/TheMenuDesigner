package ru.lavila.menudesigner.math.classifiers;

import ru.lavila.menudesigner.math.HierarchyCalculator;
import ru.lavila.menudesigner.math.ItemsListCalculator;
import ru.lavila.menudesigner.models.*;
import ru.lavila.menudesigner.models.menumodels.MenuModel;
import ru.lavila.menudesigner.utils.TheLogger;

import java.util.List;

public class HierarchyOptimizer {
    private final ItemsList itemsList;
    private final List<Hierarchy> taxonomies;
    private final Hierarchy hierarchy;
    private final HierarchyCalculator hierarchyCalculator;
    private final ItemsListCalculator calculator;
    private final CategoryEvaluator evaluator;

    public HierarchyOptimizer(ItemsList itemsList, Hierarchy hierarchy, HierarchyCalculator hierarchyCalculator, ItemsListCalculator calculator) {
        this.hierarchy = hierarchy;
        this.itemsList = itemsList;
        this.taxonomies = itemsList.getTaxonomies();
        this.hierarchyCalculator = hierarchyCalculator;
        this.calculator = calculator;
        this.evaluator = new TopDownTreeEvaluator(hierarchyCalculator);
    }

    public void optimizeSubTree(Category category) {
        long start = System.currentTimeMillis();
        MenuModel menuModel = calculator.getMenuModel();
        List<Item> group = category.getGroup();
        Item[] groupElements = group.toArray(new Item[group.size()]);

        Hierarchy bestHierarchy = itemsList.newHierarchy("Temporary", false);
        Category root = bestHierarchy.getRoot();
        bestHierarchy.add(root, groupElements);
        optimizeSubTree(bestHierarchy, root, menuModel);
        double bestTime = new HierarchyCalculator(calculator, bestHierarchy).getHierarchySearchTime();
        itemsList.removeHierarchy(bestHierarchy);

        List<MenuModel> variations = menuModel.getVariations();
        if (variations != null && !variations.isEmpty()) {
            for (MenuModel alternativeMenuModel : variations) {
                TheLogger.log("Menu Model: " + alternativeMenuModel.getName());
                calculator.setMenuModel(alternativeMenuModel);
                Hierarchy tempHierarchy = itemsList.newHierarchy("Temporary", false);
                root = tempHierarchy.getRoot();
                tempHierarchy.add(root, groupElements);
                optimizeSubTree(tempHierarchy, root, alternativeMenuModel);
                double time = new HierarchyCalculator(calculator, tempHierarchy).getHierarchySearchTime();
                itemsList.removeHierarchy(tempHierarchy);
                TheLogger.log("Hierarchy search time: " + time);
                if (time < bestTime) {
                    bestTime = time;
                    bestHierarchy = tempHierarchy;
                } else {
                    break;
                }
            }
        }

        calculator.setMenuModel(menuModel);
        new CategoryManipulator(hierarchy, category).cleanup();
        cloneCategory(bestHierarchy.getRoot(), hierarchy, category);

        TheLogger.log("Time spent (optimize sub tree): " + (System.currentTimeMillis() - start) + " ms");
    }

    private void optimizeSubTree(Hierarchy hierarchy, Category category, MenuModel menuModel) {
        CategoryManipulator manipulator = new CategoryManipulator(hierarchy, category, evaluator, menuModel);
        LocalSearchCategoryOptimizer optimizer = new LocalSearchCategoryOptimizer(manipulator, menuModel);
        Split bestSplit = manipulator.groupSplit();
        for (Hierarchy taxonomy : taxonomies) {
            Split split = optimizer.optimize(taxonomy);
            if (split.evaluation < bestSplit.evaluation) bestSplit = split;
        }
        manipulator.cleanup();
        manipulator.apply(bestSplit);
        for (Element element : category.getElements()) {
            if (element instanceof Category) {
                optimizeSubTree(hierarchy, (Category) element, menuModel);
            }
        }
    }

    private void cloneCategory(Category fromCategory, Hierarchy to, Category toCategory) {
        for (Element element : fromCategory.getElements()) {
            if (element instanceof Category) {
                cloneCategory((Category) element, to, to.newCategory(toCategory, element.getName()));
            } else {
                to.add(toCategory, element);
            }
        }
    }

    public void optimizeByTaxonomy(Category category, Hierarchy taxonomy) {
        long start = System.currentTimeMillis();
        CategoryManipulator manipulator = new CategoryManipulator(hierarchy, category, evaluator, hierarchyCalculator.getMenuModel());
        manipulator.apply(new LocalSearchCategoryOptimizer(manipulator, hierarchyCalculator.getMenuModel()).optimize(taxonomy));
        TheLogger.log("Time spent (optimize by taxonomy): " + (System.currentTimeMillis() - start) + " ms");
    }
}
