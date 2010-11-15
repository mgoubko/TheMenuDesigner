package ru.lavila.menudesigner.math.classifiers;

import ru.lavila.menudesigner.models.Category;
import ru.lavila.menudesigner.models.Element;
import ru.lavila.menudesigner.models.Hierarchy;
import ru.lavila.menudesigner.models.Item;

import java.util.*;

public class LocalSearchCategoryOptimizer extends AbstractClassifier {
    protected final CategoryEvaluator evaluator;
    protected List<Item> group;

    public LocalSearchCategoryOptimizer(Hierarchy targetHierarchy, Category category, CategoryEvaluator evaluator) {
        super(targetHierarchy, category);
        this.evaluator = evaluator;
    }

    public void optimize(Hierarchy taxonomy) {
        group = category.getGroup();
        optimizeBottomUp(taxonomy);
    }

    private void optimizeBottomUp(Hierarchy taxonomy) {
        List<Element> nextTaxonomyElements = normalizeTaxonomyElements(new ArrayList<Element>(group));
        splitByElements(nextTaxonomyElements);
        double evaluation = evaluator.evaluate(category);
        List<Element> currentTaxonomyElements;
        do {
            currentTaxonomyElements = nextTaxonomyElements;
            nextTaxonomyElements = null;
            Collection<Category> parents = new HashSet<Category>();
            for (Element element : currentTaxonomyElements) {
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
                List<Element> newTaxonomyElements = new ArrayList<Element>(currentTaxonomyElements);
                newTaxonomyElements.removeAll(parent.getElements());
                newTaxonomyElements.add(parent);
                newTaxonomyElements = normalizeTaxonomyElements(newTaxonomyElements);
                splitByElements(newTaxonomyElements);
                double newEvaluation = evaluator.evaluate(category);
                if (newEvaluation < evaluation) {
                    nextTaxonomyElements = newTaxonomyElements;
                    evaluation = newEvaluation;
                }
            }
        } while (nextTaxonomyElements != null);
        splitByElements(currentTaxonomyElements);
    }

    private void optimizeTopDown(Hierarchy taxonomy) {
        List<Element> nextTaxonomyElements = normalizeTaxonomyElements(taxonomy.getRoot().getElements());
        splitByElements(nextTaxonomyElements);
        double evaluation = evaluator.evaluate(category);
        List<Element> currentTaxonomyElements;

        do {
            currentTaxonomyElements = nextTaxonomyElements;
            nextTaxonomyElements = null;
            for (Element element : currentTaxonomyElements) {
                if (element instanceof Category) {
                    List<Element> newTaxonomyElements = new ArrayList<Element>(currentTaxonomyElements);
                    newTaxonomyElements.remove(element);
                    newTaxonomyElements.addAll(((Category) element).getElements());
                    newTaxonomyElements = normalizeTaxonomyElements(newTaxonomyElements);
                    splitByElements(newTaxonomyElements);
                    double newEvaluation = evaluator.evaluate(category);
                    if (newEvaluation < evaluation) {
                        nextTaxonomyElements = newTaxonomyElements;
                        evaluation = newEvaluation;
                    }
                }
            }
        } while (nextTaxonomyElements != null);
        splitByElements(currentTaxonomyElements);
    }

    private void optimizeTopDownSinglePath(Hierarchy taxonomy) {
        List<Element> taxonomyElements = normalizeTaxonomyElements(taxonomy.getRoot().getElements());
        splitByElements(taxonomyElements);
        int index = 0;
        double evaluation = evaluator.evaluate(category);
        while (index < taxonomyElements.size()) {
            Element element = taxonomyElements.get(index);
            if (element instanceof Category) {
                List<Element> newTaxonomyElements = new ArrayList<Element>(taxonomyElements);
                newTaxonomyElements.remove(index);
                newTaxonomyElements.addAll(((Category) element).getElements());
                newTaxonomyElements = normalizeTaxonomyElements(newTaxonomyElements);
                splitByElements(newTaxonomyElements);
                double newEvaluation = evaluator.evaluate(category);
                if (newEvaluation < evaluation) {
                    taxonomyElements = newTaxonomyElements;
                    evaluation = newEvaluation;
                    index = 0;
                } else {
                    index++;
                }
            } else {
                index++;
            }
        }
        splitByElements(taxonomyElements);
    }

    private List<Element> normalizeTaxonomyElements(List<Element> elements) {
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

    protected void splitByElements(List<Element> elements) {
        cleanupCategory();
        split = new ArrayList<Element>();
        for (Element element : elements) {
            if (element instanceof Item) {
                split.add(element);
            } else if (element instanceof Category) {
                Category newCategory = targetHierarchy.newCategory(category, element.getName());
                split.add(newCategory);
                List<Item> categoryGroup = ((Category) element).getGroup();
                categoryGroup.retainAll(group);
                targetHierarchy.add(newCategory, categoryGroup.toArray(new Item[categoryGroup.size()]));
            }
        }
        applySplitToCategory();
    }
}
