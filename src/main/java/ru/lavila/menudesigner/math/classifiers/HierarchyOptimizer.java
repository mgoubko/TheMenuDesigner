package ru.lavila.menudesigner.math.classifiers;

import ru.lavila.menudesigner.models.*;

import java.util.List;

public class HierarchyOptimizer {
    private final Hierarchy targetHierarchy;
    private final List<Hierarchy> taxonomies;
    private final CategoryEvaluator evaluator;

    public HierarchyOptimizer(Hierarchy targetHierarchy, CategoryEvaluator evaluator, ItemsList itemsList) {
        this.targetHierarchy = targetHierarchy;
        this.evaluator = evaluator;
        this.taxonomies = itemsList.getTaxonomies();
    }

    public void optimize() {
        Category category = targetHierarchy.getRoot();
        optimizeCategory(category);
    }

    private void optimizeCategory(Category category) {
        CategoryManipulator manipulator = new CategoryManipulator(targetHierarchy, category, evaluator);
        LocalSearchCategoryOptimizer optimizer = new LocalSearchCategoryOptimizer(manipulator);
        CategoryManipulator.Split bestSplit = manipulator.groupSplit();
        for (Hierarchy taxonomy : taxonomies) {
            CategoryManipulator.Split testSplit = optimizer.optimize(taxonomy);
            if (testSplit.evaluation < bestSplit.evaluation) bestSplit = testSplit;
        }
        manipulator.apply(bestSplit);
        for (Element element : category.getElements()) {
            if (element instanceof Category) {
                optimizeCategory((Category) element);
            }
        }
    }
}
