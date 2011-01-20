package ru.lavila.menudesigner.math.menumodels;

import ru.lavila.menudesigner.math.ElementsPopularityComparator;
import ru.lavila.menudesigner.models.Category;
import ru.lavila.menudesigner.models.Element;
import ru.lavila.menudesigner.models.ItemsList;
import ru.lavila.menudesigner.models.menumodels.MenuModel;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public abstract class AnonymousMenuModel implements MenuModel {
    public void init(ItemsList itemsList) {
    }

    public double getTimeToSelect(Element element, Category category) {
        List<Element> categoryElements = category.getElements();
        return getTimeToSelect(categoryElements.indexOf(element) + 1, categoryElements.size());
    }

    public Comparator<Element> getElementsComparator() {
        return getElementsComparator(null);
    }

    public Comparator<Element> getElementsComparator(PopularityCalculator popularityCalculator) {
        return new ElementsPopularityComparator(popularityCalculator);
    }

    public List<MenuModel> getVariations() {
        return Collections.emptyList();
    }
}
