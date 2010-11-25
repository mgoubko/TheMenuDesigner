package ru.lavila.menudesigner.math.classifiers;

import ru.lavila.menudesigner.models.Category;
import ru.lavila.menudesigner.models.Element;
import ru.lavila.menudesigner.models.Hierarchy;
import ru.lavila.menudesigner.models.menumodels.MenuModel;
import ru.lavila.menudesigner.utils.TheLogger;

import java.util.*;

public class LocalSearchCategoryOptimizer {
    private final CategoryManipulator manipulator;
    private final MenuModel menuModel;

    public LocalSearchCategoryOptimizer(CategoryManipulator manipulator, MenuModel menuModel) {
        this.manipulator = manipulator;
        this.menuModel = menuModel;
    }

    public Split optimize(Hierarchy taxonomy) {
        TheLogger.log("Category '" + manipulator.category.getName() + "', Taxonomy '" + taxonomy.getName() + "':");

        manipulator.cleanup();
        manipulator.hierarchy.freeze();

        Split topDownSplit = optimizeTopDown(taxonomy);
        Split best = topDownSplit;

        Split bottomUpSplit = optimizeBottomUp(taxonomy);
        if (bottomUpSplit.evaluation < best.evaluation) best = bottomUpSplit;

        Split fromTopSplit = optimizeFrom(taxonomy, topSplit(taxonomy));
        if (fromTopSplit.evaluation < best.evaluation) best = fromTopSplit;

        Split fromBottomSplit = optimizeFrom(taxonomy, manipulator.groupSplit());
        if (fromBottomSplit.evaluation < best.evaluation) best = fromBottomSplit;

        Split greedyStart = new GreedyCategoryOptimizer(manipulator, menuModel).optimize(taxonomy);
        Split fromGreedy = optimizeFrom(taxonomy, greedyStart);
        if (fromGreedy.evaluation < best.evaluation) best = fromGreedy;

        manipulator.cleanup();
        manipulator.hierarchy.unfreeze();
        
        TheLogger.log("  Top-Down:    " + topDownSplit.evaluation);
        TheLogger.log("  Bottom-Up:   " + bottomUpSplit.evaluation);
        TheLogger.log("  From Top:    " + fromTopSplit.evaluation);
        TheLogger.log("  From Bottom: " + fromBottomSplit.evaluation);
        TheLogger.log("  From Greedy: " + fromGreedy.evaluation);

        return best;
    }

    private Split topSplit(Hierarchy taxonomy) {
        Split split = manipulator.split(taxonomy.getRoot().getElements());
        while (split.elements.size() == 1 && (split.elements.get(0) instanceof Category)) {
            split = manipulator.split(((Category) split.elements.get(0)).getElements());
        }
        return split;
    }

    private Split optimizeFrom(Hierarchy taxonomy, Split start) {
        TheLogger.log("  From...");
        Split nextSplit = start;
        Split currentSplit;
        do {
            currentSplit = nextSplit;
            TheLogger.log("    " + currentSplit.toString());
            Split upSplit = stepUp(taxonomy, currentSplit);
            Split downSplit = stepDown(currentSplit);
            nextSplit = upSplit.evaluation < downSplit.evaluation ? upSplit : downSplit;
        } while (nextSplit != currentSplit);
        return currentSplit;
    }

    private Split optimizeBottomUp(Hierarchy taxonomy) {
        TheLogger.log("  Bottom-Up...");
        Split nextSplit = manipulator.groupSplit();
        Split currentSplit;
        do {
            currentSplit = nextSplit;
            TheLogger.log("    " + currentSplit.toString());
            nextSplit = stepUp(taxonomy, currentSplit);
        } while (nextSplit != currentSplit);
        return currentSplit;
    }

    private Split stepUp(Hierarchy taxonomy, Split start) {
        Split result = start;
        Collection<Category> parents = new HashSet<Category>();
        for (Element element : start.elements) {
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
            List<Element> testSplitElements = new ArrayList<Element>(start.elements);
            testSplitElements.removeAll(parent.getElements());
            testSplitElements.add(parent);
            if (testSplitElements.size() > 1) {
                Split testSplit = manipulator.split(testSplitElements);
                if (testSplit.evaluation < result.evaluation) result = testSplit;
            }
        }
        return result;
    }

    private Split optimizeTopDown(Hierarchy taxonomy) {
        TheLogger.log("  Top-Down...");
        Split nextSplit = topSplit(taxonomy);
        Split currentSplit;
        do {
            currentSplit = nextSplit;
            TheLogger.log("    " + currentSplit.toString());
            nextSplit = stepDown(currentSplit);
        } while (nextSplit != currentSplit);
        return currentSplit;
    }

    private Split stepDown(Split start) {
        Split result = start;
        for (Element element : start.elements) {
            if (element instanceof Category) {
                List<Element> testSplitElements = new ArrayList<Element>(start.elements);
                testSplitElements.remove(element);
                testSplitElements.addAll(((Category) element).getElements());
                Split testSplit = manipulator.split(testSplitElements);
                if (testSplit.evaluation < result.evaluation) result = testSplit;
            }
        }
        return result;
    }

    private Split optimizeTopDownSinglePath(Hierarchy taxonomy) {
        Split split = manipulator.split(taxonomy.getRoot().getElements());
        int index = 0;
        while (index < split.elements.size()) {
            Element element = split.elements.get(index);
            if (element instanceof Category) {
                List<Element> testSplitElements = new ArrayList<Element>(split.elements);
                testSplitElements.remove(index);
                testSplitElements.addAll(((Category) element).getElements());
                Split testSplit = manipulator.split(testSplitElements);
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
