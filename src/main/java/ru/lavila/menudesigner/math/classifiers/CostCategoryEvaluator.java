package ru.lavila.menudesigner.math.classifiers;

import ru.lavila.menudesigner.math.ItemsListCalculator;
import ru.lavila.menudesigner.models.Category;

public class CostCategoryEvaluator extends AbstractCategoryEvaluator {
    public CostCategoryEvaluator(ItemsListCalculator itemsCalculator) {
        super(itemsCalculator);
    }

    public double evaluate(Category category) {
        return evaluate(category, new ChildEvaluator());
    }

    private class ChildEvaluator implements CategoryEvaluator {
        public double evaluate(Category category) {
            return itemsCalculator.getOptimalSearchTime(category.getGroup());
        }
    }
}
