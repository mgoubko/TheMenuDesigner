package ru.lavila.menudesigner.math.classifiers;

import ru.lavila.menudesigner.math.ItemsListCalculator;
import ru.lavila.menudesigner.models.Category;
import ru.lavila.menudesigner.models.Element;

import java.util.List;

public abstract class AbstractCategoryEvaluator implements CategoryEvaluator {
    protected final ItemsListCalculator itemsCalculator;

    public AbstractCategoryEvaluator(ItemsListCalculator itemsCalculator) {
        this.itemsCalculator = itemsCalculator;
    }

    public double evaluate(Category category, CategoryEvaluator childEvaluator) {
        double result = 0;
        List<Element> elements = category.getElements();
        int totalElements = elements.size();
        for (int index = 0; index < totalElements; index++)
        {
            Element element = elements.get(index);
            result += itemsCalculator.getMenuModel().getTimeToSelect(index + 1, totalElements) * element.getPopularity();
            if (element instanceof Category) result += childEvaluator.evaluate((Category) element);
        }
        return result;
    }
}
