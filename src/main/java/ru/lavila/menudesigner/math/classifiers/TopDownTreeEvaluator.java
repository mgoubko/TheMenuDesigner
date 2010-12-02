package ru.lavila.menudesigner.math.classifiers;

import ru.lavila.menudesigner.math.HierarchyCalculator;
import ru.lavila.menudesigner.models.Category;
import ru.lavila.menudesigner.models.Item;
import ru.lavila.menudesigner.models.menumodels.MenuModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TopDownTreeEvaluator extends AbstractCategoryEvaluator {

    public TopDownTreeEvaluator(HierarchyCalculator hierarchyCalculator) {
        super(hierarchyCalculator);
    }

    public double evaluate(Category category) {
        return evaluate(category, new ChildEvaluator(calculator.getMenuModel()));
    }

    private class ChildEvaluator implements CategoryEvaluator {
        private final MenuModel menuModel;

        public ChildEvaluator(MenuModel menuModel) {
            this.menuModel = menuModel;
        }

        public double evaluate(List<Item> items, double popularity) {
            double[] proportion = menuModel.getOptimalProportion();
            int index = 0;
            CountedItems countedItems = new CountedItems();
            List<CountedItems> split = new ArrayList<CountedItems>();
            split.add(countedItems);
            double setLimit = proportion[index] * popularity;
            int itemIndex = 0;
            while (itemIndex < items.size()) {
                Item item = items.get(itemIndex);
                if ((countedItems.popularity < setLimit) && (countedItems.popularity + item.getPopularity() - setLimit < setLimit - countedItems.popularity)) {
                    countedItems.add(item);
                    itemIndex++;
                } else {
                    if (countedItems.items.isEmpty()) split.remove(countedItems);
                    setLimit += proportion[++index] * popularity - countedItems.popularity;
                    countedItems = new CountedItems();
                    split.add(countedItems);
                }
            }
            if (countedItems.items.isEmpty()) split.remove(countedItems);

            double evaluation = 0;
            for (index = 0; index < split.size(); index++) {
                CountedItems splitItems = split.get(index);
                evaluation += menuModel.getTimeToSelect(index + 1, split.size()) * splitItems.popularity;
                if (splitItems.items.size() > 1) evaluation += evaluate(splitItems.items, splitItems.popularity);
            }
            return evaluation;
        }

        public double evaluate(Category category) {
            List<Item> group = category.getGroup();
            Collections.sort(group, new ElementsPopularityComparator());
            double popularity = 0;
            for (Item item : group) popularity += item.getPopularity();
            return evaluate(group, popularity);
        }
    }

    private static class CountedItems {
        public final List<Item> items;
        public double popularity;

        public CountedItems() {
            items = new ArrayList<Item>();
            popularity = 0;
        }

        public void add(Item item) {
            items.add(item);
            popularity += item.getPopularity();
        }
    }
}
