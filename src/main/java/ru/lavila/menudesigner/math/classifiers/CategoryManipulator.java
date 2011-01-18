package ru.lavila.menudesigner.math.classifiers;

import ru.lavila.menudesigner.math.ElementsPopularityComparator;
import ru.lavila.menudesigner.models.Category;
import ru.lavila.menudesigner.models.Element;
import ru.lavila.menudesigner.models.Hierarchy;
import ru.lavila.menudesigner.models.Item;
import ru.lavila.menudesigner.models.menumodels.MenuModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CategoryManipulator {
    public final Hierarchy hierarchy;
    public final Category category;
    public final List<Item> group;
    public final CategoryEvaluator evaluator;
    private final Comparator<Element> elementsComparator;

    public CategoryManipulator(Hierarchy hierarchy, Category category) {
        this(hierarchy, category, null, null);
    }

    public CategoryManipulator(Hierarchy hierarchy, Category category, CategoryEvaluator evaluator, MenuModel menuModel) {
        this.hierarchy = hierarchy;
        this.category = category;
        this.evaluator = evaluator;
        this.group = category.getGroup();
        elementsComparator = menuModel.getElementsComparator(new PopularityCalculator());
    }

    protected void cleanup() {
        List<Element> children = category.getElements();
        hierarchy.remove(children.toArray(new Element[children.size()]));
    }

    public void sortByPopularity()
    {
        List<Element> elements = new ArrayList<Element>(category.getElements());
        Collections.sort(elements, new ElementsPopularityComparator());
        hierarchy.add(category, elements.toArray(new Element[elements.size()]));
    }

    public void apply(Split split) {
        apply(split.elements);
    }

    public void apply(List<Element> splitElements) {
        cleanup();
        List<Element> elements = new ArrayList<Element>();
        for (Element element : splitElements) {
            if (element instanceof Item) {
                elements.add(element);
            } else if (element instanceof Category) {
                Category newCategory = hierarchy.newCategory(category, element.getName());
                elements.add(newCategory);
                List<Item> categoryGroup = ((Category) element).getGroup();
                categoryGroup.retainAll(group);
                hierarchy.add(newCategory, categoryGroup.toArray(new Item[categoryGroup.size()]));
            }
        }
        hierarchy.add(category, elements.toArray(new Element[elements.size()]));
    }

    private List<Element> normalizeSplitElements(List<Element> elements) {
        List<Element> normalized = new ArrayList<Element>();
        for (Element element : elements) {
            if (element instanceof Item) {
                if (group.contains(element)) normalized.add(element);
            } else if (element instanceof Category) {
                List<Element> childElements = getChildElements(element);
                if (!childElements.isEmpty()) {
                    while (childElements.size() == 1) {
                        element = childElements.get(0);
                        childElements = getChildElements(element);
                    }
                    normalized.add(element);
                }
            }
        }
        Collections.sort(normalized, elementsComparator);
        return normalized;
    }

    private List<Element> getChildElements(Element element) {
        List<Element> childElements = new ArrayList<Element>();
        if (element instanceof Category) {
            for (Element childElement : ((Category) element).getElements()) {
                if (childElement instanceof Item) {
                    if (group.contains(childElement)) childElements.add(childElement);
                } else if (childElement instanceof Category) {
                    List<Item> childGroup = ((Category) childElement).getGroup();
                    childGroup.retainAll(group);
                    if (!childGroup.isEmpty()) childElements.add(childElement);
                }
            }
        }
        return childElements;
    }

    public Split split(List<Element> elements) {
        double evaluation = -1;
        List<Element> normalized = normalizeSplitElements(elements);
        if (evaluator != null) {
            apply(normalized);
            evaluation = evaluator.evaluate(category);
        }
        return new Split(normalized, evaluation);
    }

    public Split groupSplit() {
        return split(new ArrayList<Element>(group));
    }

    private class PopularityCalculator implements MenuModel.PopularityCalculator {
        public double getPopularity(Element element) {
            if (element instanceof Item && group.contains(element)) {
                return element.getPopularity();
            } else if (element instanceof Category) {
                List<Item> categoryGroup = ((Category) element).getGroup();
                categoryGroup.retainAll(group);
                double popularity = 0;
                for (Item item : categoryGroup) {
                    popularity += item.getPopularity();
                }
                return popularity;
            }
            return 0;
        }
    }
}
