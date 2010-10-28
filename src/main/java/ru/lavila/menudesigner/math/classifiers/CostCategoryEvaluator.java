package ru.lavila.menudesigner.math.classifiers;

import ru.lavila.menudesigner.math.HierarchyCalculator;
import ru.lavila.menudesigner.models.Category;

public class CostCategoryEvaluator implements CategoryEvaluator
{
    private final HierarchyCalculator calculator;

    public CostCategoryEvaluator(HierarchyCalculator calculator)
    {
        this.calculator = calculator;
    }

    public double evaluate(Category category)
    {
        return calculator.getSearchTimeEvaluation(category);
    }
}
