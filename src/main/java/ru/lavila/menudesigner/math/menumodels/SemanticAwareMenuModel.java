package ru.lavila.menudesigner.math.menumodels;

import ru.lavila.menudesigner.math.ElementsPopularityComparator;
import ru.lavila.menudesigner.models.*;

import java.util.Comparator;
import java.util.List;

public class SemanticAwareMenuModel extends ReadUntilMenuModel {
    public SemanticAwareMenuModel(double tResp, double tLoad, double tClick) {
        super(tResp, tLoad, -1, tClick);
    }

    @Override
    public String getName() {
        return "Semantic-aware self-terminating serial search";
    }

    @Override
    public void init(ItemsList itemsList) {
        super.init(itemsList);
        optimalProportion = null;
        tRead = -1;
        for (Item item : itemsList.toArray()) {
            double readingTime = item.getReadingTime();
            if (readingTime < tRead || tRead < 0) tRead = readingTime;
        }
        for (Hierarchy taxonomy : itemsList.getTaxonomies()) {
            for (Category category : taxonomy.getAllCategories()) {
                double readingTime = category.getReadingTime();
                if (readingTime < tRead) tRead = readingTime;
            }
        }
    }

    @Override
    public double getTimeToSelect(int target, int total) {
        validateInit();
        return super.getTimeToSelect(target, total);
    }

    @Override
    public double getTimeToSelect(Element element, Category category) {
        List<Element> elements = category.getElements();
        double result = tResp + tLoad * elements.size() + tClick;
        for (Element categoryElement : elements) {
            result += categoryElement.getReadingTime();
            if (categoryElement == element) break;
        }
        return result;
    }

    @Override
    public double[] getOptimalProportion() {
        validateInit();
        return super.getOptimalProportion();
    }

    @Override
    public Comparator<Element> getElementsComparator(PopularityCalculator popularityCalculator) {
        return new ElementsComparator(popularityCalculator);
    }

    protected void validateInit() {
        if (tRead < 0) throw new IllegalStateException("Menu model is not initialized");
    }

    private static class ElementsComparator extends ElementsPopularityComparator {
        private ElementsComparator(PopularityCalculator popularityCalculator) {
            super(popularityCalculator);
        }

        @Override
        public int compare(Element element1, Element element2) {
            return Double.compare(
                    popularityCalculator.getPopularity(element2) / element2.getReadingTime(),
                    popularityCalculator.getPopularity(element1) / element1.getReadingTime()
            );
        }
    }
}
