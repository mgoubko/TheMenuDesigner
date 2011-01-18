package ru.lavila.menudesigner.math.classifiers;

import ru.lavila.menudesigner.math.HierarchyCalculator;
import ru.lavila.menudesigner.models.Category;
import ru.lavila.menudesigner.models.Element;

public abstract class AbstractCategoryEvaluator implements CategoryEvaluator {
    protected final HierarchyCalculator calculator;

    public AbstractCategoryEvaluator(HierarchyCalculator calculator) {
        this.calculator = calculator;
    }

    public double evaluate(Category category, CategoryEvaluator childEvaluator) {
        double result = 0;
        for (Element element : category.getElements()) {
            result += calculator.getMenuModel().getTimeToSelect(element, category) * element.getPopularity();
            if (element instanceof Category) result += childEvaluator.evaluate((Category) element);
        }
        return result;
    }
}
