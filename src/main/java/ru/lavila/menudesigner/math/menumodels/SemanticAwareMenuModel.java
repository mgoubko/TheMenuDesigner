package ru.lavila.menudesigner.math.menumodels;

import ru.lavila.menudesigner.math.ElementsPopularityComparator;
import ru.lavila.menudesigner.models.*;
import ru.lavila.menudesigner.models.menumodels.MenuModel;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SemanticAwareMenuModel extends ReadUntilWithErrorMenuModel {
    private final int VARIATIONS = 10;
    private final int variation;

    public SemanticAwareMenuModel(double tResp, double tLoad, double tClick, double errorProbability) {
        this(tResp, tLoad, tClick, errorProbability, 0);
    }

    private SemanticAwareMenuModel(double tResp, double tLoad, double tClick, double errorProbability, int variation) {
        super(tResp, tLoad, -1, tClick, errorProbability);
        this.variation = variation;
    }

    @Override
    public String getName() {
        String name = "Semantic-aware " + super.getName();
        if (variation > 0) name += " (" + variation + "/" + VARIATIONS + ")";
        return name;
    }

    @Override
    public void init(ItemsList itemsList) {
        super.init(itemsList);
        optimalProportion = null;
        double minReadingTime = -1;
        double maxReadingTime = -1;
        for (Item item : itemsList.toArray()) {
            double readingTime = item.getReadingTime();
            if (readingTime < minReadingTime || minReadingTime < 0) minReadingTime = readingTime;
            if (readingTime > maxReadingTime || maxReadingTime < 0) maxReadingTime = readingTime;
        }
        for (Hierarchy taxonomy : itemsList.getTaxonomies()) {
            for (Category category : taxonomy.getAllCategories()) {
                // Skip root category as it's name should never be used in the target menu
                if (taxonomy.getRoot() == category) continue;
                double readingTime = category.getReadingTime();
                if (readingTime < minReadingTime) minReadingTime = readingTime;
                if (readingTime > maxReadingTime) maxReadingTime = readingTime;
            }
        }
        tRead = minReadingTime + (maxReadingTime - minReadingTime) * variation / VARIATIONS;
    }

    @Override
    public double getTimeToSelect(int target, int total) {
        validateInit();
        return super.getTimeToSelect(target, total);
    }

    @Override
    public double getTimeToSelect(Element element, Category category) {
        List<Element> elements = category.getElements();
        double result = (1 + 2 * errorProbability) * (tResp + tLoad * elements.size() + tClick);
        boolean willRead = true;
        for (Element categoryElement : elements) {
            result += categoryElement.getReadingTime() * (willRead ? 1 + 2 * errorProbability : errorProbability);
            if (categoryElement == element) willRead = false;
        }
        return result;
    }

    @Override
    public double[] getOptimalProportion() {
        validateInit();
        return super.getOptimalProportion();
    }

    @Override
    public List<MenuModel> getVariations() {
        List<MenuModel> variations = new ArrayList<MenuModel>();
        for (int i = 1; i <= VARIATIONS; i++) {
            variations.add(new SemanticAwareMenuModel(tResp, tLoad, tClick, errorProbability, i));
        }
        return variations;
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
