package ru.lavila.menudesigner.math;

import ru.lavila.menudesigner.models.Element;
import ru.lavila.menudesigner.models.menumodels.MenuModel;

import java.util.Comparator;

public class ElementsPopularityComparator implements Comparator<Element> {
    protected MenuModel.PopularityCalculator popularityCalculator;

    public ElementsPopularityComparator() {
        this(null);
    }

    public ElementsPopularityComparator(MenuModel.PopularityCalculator popularityCalculator) {
        this.popularityCalculator = popularityCalculator == null ? new DefaultPopularityCalculator() : popularityCalculator;
    }

    public int compare(Element element1, Element element2) {
        return Double.compare(
                popularityCalculator.getPopularity(element2),
                popularityCalculator.getPopularity(element1)
        );
    }

    protected static class DefaultPopularityCalculator implements MenuModel.PopularityCalculator {
        public double getPopularity(Element element) {
            return element.getPopularity();
        }
    }
}
