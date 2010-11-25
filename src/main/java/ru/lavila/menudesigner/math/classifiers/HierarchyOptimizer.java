package ru.lavila.menudesigner.math.classifiers;

import ru.lavila.menudesigner.math.HierarchyCalculator;
import ru.lavila.menudesigner.models.*;

import java.util.List;

public class HierarchyOptimizer {
    private final List<Hierarchy> taxonomies;
    private final Hierarchy hierarchy;
    private final HierarchyCalculator calculator;

    public HierarchyOptimizer(ItemsList itemsList, Hierarchy hierarchy, HierarchyCalculator calculator) {
        this.hierarchy = hierarchy;
        this.taxonomies = itemsList.getTaxonomies();
        this.calculator = calculator;
    }

    public void optimize() {
        Category category = hierarchy.getRoot();
        optimizeCategory(category);
    }

    private void optimizeCategory(Category category) {
        CategoryEvaluator evaluator = new TopDownTreeEvaluator(hierarchy, calculator);
//        CategoryEvaluator evaluator = new CostCategoryEvaluator(calculator);
        CategoryManipulator manipulator = new CategoryManipulator(hierarchy, category, evaluator);
        LocalSearchCategoryOptimizer optimizer = new LocalSearchCategoryOptimizer(manipulator, calculator.getMenuModel());
        manipulator.cleanup();
        hierarchy.freeze();
        Split bestSplit = manipulator.groupSplit();
        for (Hierarchy taxonomy : taxonomies) {
            Split testSplit = optimizer.optimize(taxonomy);
            if (testSplit.evaluation < bestSplit.evaluation) bestSplit = testSplit;
        }
        manipulator.cleanup();
        hierarchy.unfreeze();
        manipulator.apply(bestSplit);
        for (Element element : category.getElements()) {
            if (element instanceof Category) {
                optimizeCategory((Category) element);
            }
        }
    }
}
