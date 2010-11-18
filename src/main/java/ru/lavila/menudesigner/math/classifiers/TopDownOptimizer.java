package ru.lavila.menudesigner.math.classifiers;

import ru.lavila.menudesigner.models.Category;
import ru.lavila.menudesigner.models.Hierarchy;
import ru.lavila.menudesigner.models.Item;
import ru.lavila.menudesigner.models.menumodels.MenuModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TopDownOptimizer {
    private final CategoryManipulator manipulator;
    private final Hierarchy hierarchy;
    private final Category category;

    public TopDownOptimizer(CategoryManipulator manipulator) {
        this.manipulator = manipulator;
        this.hierarchy = manipulator.hierarchy;
        this.category = manipulator.category;
    }

    public void optimize(MenuModel menuModel) {
        List<Item> group = category.getGroup();
        Collections.sort(group, new ElementsPopularityComparator());
        manipulator.cleanup();
        double[] proportion = menuModel.getOptimalProportion();
        double groupPopularity = 0;
        for (Item item : group) groupPopularity += item.getPopularity();
        double proportionSplit = 0;
        double usedPopularity = 0;
        for (int proportionIndex = 0; proportionIndex < proportion.length; proportionIndex++) {
            proportionSplit += proportion[proportionIndex] * groupPopularity;
            List<Item> items = new ArrayList<Item>();
            Item item = null;
            for (int index = 0; index < group.size() && usedPopularity < proportionSplit; index++) {
                item = group.get(index);
                items.add(item);
                usedPopularity += item.getPopularity();
            }
            if (item != null && (usedPopularity - proportionSplit) > (proportionSplit - usedPopularity + item.getPopularity())) {
                items.remove(item);
                usedPopularity -= item.getPopularity();
            }
            group.removeAll(items);
            if (!items.isEmpty()) {
                if (items.size() == 1) {
                    hierarchy.add(category, items.get(0));
                } else {
                    Category newCategory = hierarchy.newCategory(category, "Category " + proportionIndex);
                    hierarchy.add(newCategory, items.toArray(new Item[items.size()]));
                    new TopDownOptimizer(new CategoryManipulator(hierarchy, newCategory)).optimize(menuModel);
                }
            }
        }
    }
}
