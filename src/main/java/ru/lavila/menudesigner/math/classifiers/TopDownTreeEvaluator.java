package ru.lavila.menudesigner.math.classifiers;

import ru.lavila.menudesigner.math.HierarchyCalculator;
import ru.lavila.menudesigner.math.ItemsListCalculator;
import ru.lavila.menudesigner.models.Category;
import ru.lavila.menudesigner.models.Hierarchy;

public class TopDownTreeEvaluator extends AbstractCategoryEvaluator {
    private final Hierarchy targetHierarchy;
    private final HierarchyCalculator hierarchyCalculator;

    public TopDownTreeEvaluator(ItemsListCalculator itemsCalculator, Hierarchy targetHierarchy, HierarchyCalculator hierarchyCalculator) {
        super(itemsCalculator);
        this.targetHierarchy = targetHierarchy;
        this.hierarchyCalculator = hierarchyCalculator;
    }

    public double evaluate(Category category) {
        return evaluate(category, new ChildEvaluator());
    }

    private class ChildEvaluator implements CategoryEvaluator {
        public double evaluate(Category category) {
            new TopDownOptimizer(targetHierarchy, category).optimize(itemsCalculator.getMenuModel());
            return hierarchyCalculator.getSearchTime(category);
        }
    }
}
