package ru.lavila.menudesigner.math.classifiers;

import ru.lavila.menudesigner.math.HierarchyCalculator;
import ru.lavila.menudesigner.models.Category;
import ru.lavila.menudesigner.models.Item;

import java.util.List;

public class CostCategoryEvaluator extends AbstractCategoryEvaluator {
    private final double childFactor;
    private final double childFactorGrowth;
    private final int itemsCount;

    public CostCategoryEvaluator(HierarchyCalculator calculator) {
        this(calculator, 1, 0, 1);
    }

    public CostCategoryEvaluator(HierarchyCalculator calculator, double childFactor, double childFactorGrowth, int itemsCount) {
        super(calculator);
        this.childFactor = childFactor;
        this.childFactorGrowth = childFactorGrowth;
        this.itemsCount = itemsCount;
    }

    public double evaluate(Category category) {
        return evaluate(category, new ChildEvaluator());
    }

    private class ChildEvaluator implements CategoryEvaluator {
        public double evaluate(Category category) {
            List<Item> group = category.getGroup();
            return (childFactor + childFactorGrowth * (itemsCount - group.size()) / itemsCount) * calculator.getOptimalSearchTime(group);
        }
    }
}
