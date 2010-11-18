package ru.lavila.menudesigner.math.classifiers;

import ru.lavila.menudesigner.models.Category;
import ru.lavila.menudesigner.models.Element;
import ru.lavila.menudesigner.models.Hierarchy;
import ru.lavila.menudesigner.models.Item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CategoryManipulator {
    public final Hierarchy hierarchy;
    public final Category category;
    public final List<Item> group;
    public final CategoryEvaluator evaluator;

    public CategoryManipulator(Hierarchy hierarchy, Category category, CategoryEvaluator evaluator) {
        this.hierarchy = hierarchy;
        this.category = category;
        this.evaluator = evaluator;
        this.group = category.getGroup();
    }

    protected void cleanup() {
        List<Element> children = category.getElements();
        hierarchy.remove(children.toArray(new Element[children.size()]));
    }

    public void apply(Split split) {
        cleanup();
        List<Element> elements = new ArrayList<Element>();
        for (Element element : split.elements) {
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
        Collections.sort(normalized, new Comparator<Element>() {
            public int compare(Element element1, Element element2) {
                return Double.compare(getPopularity(element2), getPopularity(element1));
            }

            private double getPopularity(Element element) {
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
        });
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
        return new Split(elements);
    }

    public Split groupSplit() {
        return new Split(new ArrayList<Element>(group));
    }

    class Split {
        public final List<Element> elements;
        public final double evaluation;

        public Split(List<Element> elements) {
            this.elements = normalizeSplitElements(elements);
            this.evaluation = evaluate();
        }

        private double evaluate() {
            apply(this);
            return evaluator.evaluate(category);
        }
    }
}
