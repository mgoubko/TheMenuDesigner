package ru.lavila.menudesigner.math.classifiers;

import ru.lavila.menudesigner.math.HierarchyCalculator;
import ru.lavila.menudesigner.models.Category;

public class CostCategoryEvaluator extends AbstractCategoryEvaluator {
    public CostCategoryEvaluator(HierarchyCalculator calculator) {
        super(calculator);
    }

    public double evaluate(Category category) {
        return evaluate(category, new ChildEvaluator());
    }

    private class ChildEvaluator implements CategoryEvaluator {
        public double evaluate(Category category) {
            return calculator.getOptimalSearchTime(category.getGroup());
        }
    }
}
