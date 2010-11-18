package ru.lavila.menudesigner.math.classifiers;

import ru.lavila.menudesigner.models.Category;
import ru.lavila.menudesigner.models.Element;
import ru.lavila.menudesigner.models.Hierarchy;
import ru.lavila.menudesigner.utils.TheLogger;

import java.util.*;

public class LocalSearchCategoryOptimizer {
    private final CategoryManipulator manipulator;

    public LocalSearchCategoryOptimizer(CategoryManipulator manipulator) {
        this.manipulator = manipulator;
    }

    public CategoryManipulator.Split optimize(Hierarchy taxonomy) {
        CategoryManipulator.Split topDownSplit = optimizeTopDown(taxonomy);
        CategoryManipulator.Split bottomUpSplit = optimizeBottomUp(taxonomy);
        TheLogger.log("Category '" + manipulator.category.getName() + "', Taxonomy '" + taxonomy.getName() + "':");
        TheLogger.log("  Top-Down:  " + topDownSplit.evaluation);
        TheLogger.log("  Bottom-Up: " + bottomUpSplit.evaluation);
        return bottomUpSplit.evaluation < topDownSplit.evaluation ? bottomUpSplit : topDownSplit;
    }

    private CategoryManipulator.Split optimizeBottomUp(Hierarchy taxonomy) {
        CategoryManipulator.Split nextSplit = manipulator.groupSplit();
        CategoryManipulator.Split currentSplit;
        do {
            currentSplit = nextSplit;
            Collection<Category> parents = new HashSet<Category>();
            for (Element element : currentSplit.elements) {
                parents.add(taxonomy.getElementCategory(element));
            }
            parents.remove(null);
            for (Category parent : parents) {
                boolean valid = true;
                for (Element child : parent.getElements()) {
                    if (parents.contains(child)) {
                        valid = false;
                        break;
                    }
                }
                if (!valid) continue;
                List<Element> testSplitElements = new ArrayList<Element>(currentSplit.elements);
                testSplitElements.removeAll(parent.getElements());
                testSplitElements.add(parent);
                CategoryManipulator.Split testSplit = manipulator.split(testSplitElements);
                if (testSplit.evaluation < nextSplit.evaluation) nextSplit = testSplit;
            }
        } while (nextSplit != currentSplit);
        return currentSplit;
    }

    private CategoryManipulator.Split optimizeTopDown(Hierarchy taxonomy) {
        CategoryManipulator.Split nextSplit = manipulator.split(taxonomy.getRoot().getElements());
        CategoryManipulator.Split currentSplit;
        do {
            currentSplit = nextSplit;
            for (Element element : currentSplit.elements) {
                if (element instanceof Category) {
                    List<Element> testSplitElements = new ArrayList<Element>(currentSplit.elements);
                    testSplitElements.remove(element);
                    testSplitElements.addAll(((Category) element).getElements());
                    CategoryManipulator.Split testSplit = manipulator.split(testSplitElements);
                    if (testSplit.evaluation < nextSplit.evaluation) nextSplit = testSplit;
                }
            }
        } while (nextSplit != currentSplit);
        return currentSplit;
    }

    private CategoryManipulator.Split optimizeTopDownSinglePath(Hierarchy taxonomy) {
        CategoryManipulator.Split split = manipulator.split(taxonomy.getRoot().getElements());
        int index = 0;
        while (index < split.elements.size()) {
            Element element = split.elements.get(index);
            if (element instanceof Category) {
                List<Element> testSplitElements = new ArrayList<Element>(split.elements);
                testSplitElements.remove(index);
                testSplitElements.addAll(((Category) element).getElements());
                CategoryManipulator.Split testSplit = manipulator.split(testSplitElements);
                if (testSplit.evaluation < split.evaluation) {
                    split = testSplit;
                    index = 0;
                } else {
                    index++;
                }
            } else {
                index++;
            }
        }
        return split;
    }
}
