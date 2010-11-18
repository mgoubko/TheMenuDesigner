package ru.lavila.menudesigner.math.classifiers;

import ru.lavila.menudesigner.math.HierarchyCalculator;
import ru.lavila.menudesigner.models.Category;
import ru.lavila.menudesigner.models.Hierarchy;

public class TopDownTreeEvaluator extends AbstractCategoryEvaluator {
    private final Hierarchy targetHierarchy;
    private final HierarchyCalculator hierarchyCalculator;

    public TopDownTreeEvaluator(Hierarchy targetHierarchy, HierarchyCalculator hierarchyCalculator) {
        super(hierarchyCalculator);
        this.targetHierarchy = targetHierarchy;
        this.hierarchyCalculator = hierarchyCalculator;
    }

    public double evaluate(Category category) {
        return evaluate(category, new ChildEvaluator());
    }

    private class ChildEvaluator implements CategoryEvaluator {
        public double evaluate(Category category) {
            new TopDownOptimizer(new CategoryManipulator(targetHierarchy, category)).optimize(calculator.getMenuModel());
            return hierarchyCalculator.getSearchTime(category);
        }
    }
}
