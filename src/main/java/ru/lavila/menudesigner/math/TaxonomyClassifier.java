package ru.lavila.menudesigner.math;

import ru.lavila.menudesigner.models.Category;
import ru.lavila.menudesigner.models.Element;
import ru.lavila.menudesigner.models.Hierarchy;
import ru.lavila.menudesigner.models.Item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TaxonomyClassifier
{
    private final MenuModel menuModel;
    private final Hierarchy targetHierarchy;

    public TaxonomyClassifier(MenuModel menuModel, Hierarchy targetHierarchy)
    {
        this.menuModel = menuModel;
        this.targetHierarchy = targetHierarchy;
    }

    public void classifyByTaxonomy(Hierarchy taxonomy, Category category)
    {
        List<Item> group = category.getGroup();
        List<Element> children = category.getElements();
        targetHierarchy.remove(children.toArray(new Element[children.size()]));
        double groupPopularity = 0;
        for (Item item : group)
        {
            groupPopularity += item.getPopularity();
        }
        List<TaxonomyElement> taxonomyElements = collectTaxonomyElements(taxonomy.getRoot(), group);
        double[] proportion = menuModel.getOptimalProportion();
        List<Element> split = new ArrayList<Element>();
        for (int index = 0; index < proportion.length - 1; index++)
        {
            if (taxonomyElements.isEmpty()) break;
            TaxonomyElement taxonomyElement = findClosestTaxonomyElement(taxonomyElements, groupPopularity * proportion[index]);
            taxonomyElements.remove(taxonomyElement);
            taxonomyElements.removeAll(taxonomyElement.parents);
            taxonomyElements.removeAll(taxonomyElement.children);
            if (taxonomyElement.element instanceof Category)
            {
                Category newCategory = targetHierarchy.newCategory(category, taxonomyElement.element.getName());
                split.add(newCategory);
                List<Item> items = new ArrayList<Item>();
                for (TaxonomyElement child : taxonomyElement.children)
                {
                    if (child.element instanceof Item)
                    {
                        items.add((Item) child.element);
                    }
                }
                targetHierarchy.add(newCategory, items.toArray(new Item[items.size()]));
            }
            else
            {
                targetHierarchy.add(category, taxonomyElement.element);
                split.add(taxonomyElement.element);
            }
        }
        if (taxonomyElements.size() == 1)
        {
            targetHierarchy.add(category, taxonomyElements.get(0).element);
            split.add(taxonomyElements.get(0).element);
        }
        else if (taxonomyElements.size() > 1)
        {
            Category newCategory = targetHierarchy.newCategory(category, "...");
            split.add(newCategory);
            for (TaxonomyElement taxonomyElement : taxonomyElements)
            {
                if (taxonomyElement.element instanceof Item)
                {
                    targetHierarchy.add(newCategory, taxonomyElement.element);
                }
            }
        }
        targetHierarchy.add(category, split.toArray(new Element[split.size()]));
    }

    private TaxonomyElement findClosestTaxonomyElement(List<TaxonomyElement> taxonomyElements, double value)
    {
        double lastDiff = -1;
        TaxonomyElement lastElement = null;
        for (TaxonomyElement taxonomyElement : taxonomyElements)
        {
            double diff = taxonomyElement.popularity - value;
            if (diff > 0)
            {
                if (taxonomyElement.element instanceof Item) return taxonomyElement;
                lastDiff = diff;
                lastElement = taxonomyElement;
            }
            else
            {
                return (lastDiff < -diff && lastElement != null) ? lastElement : taxonomyElement;
            }
        }
        return lastElement;
    }

    private List<TaxonomyElement> collectTaxonomyElements(Category taxonomyCategory, List<Item> targetItems)
    {
        List<TaxonomyElement> taxonomyElements = new ArrayList<TaxonomyElement>();
        for (Element element : taxonomyCategory.getElements())
        {
            if (element instanceof Item && targetItems.contains(element))
            {
                taxonomyElements.add(new TaxonomyElement(element));
            }
            else if (element instanceof Category)
            {
                Category category = (Category) element;
                List<TaxonomyElement> childTaxonomyElements = collectTaxonomyElements(category, targetItems);
                if (!childTaxonomyElements.isEmpty())
                {
                    TaxonomyElement categoryTaxonomyElement = new TaxonomyElement(category);
                    taxonomyElements.add(categoryTaxonomyElement);
                    for (TaxonomyElement taxonomyElement : childTaxonomyElements)
                    {
                        categoryTaxonomyElement.addChild(taxonomyElement);
                        taxonomyElement.addParent(categoryTaxonomyElement);
                        taxonomyElements.add(taxonomyElement);
                    }
                }
            }
        }
        Collections.sort(taxonomyElements);
        return taxonomyElements;
    }

    private static class TaxonomyElement implements Comparable
    {
        private final Element element;
        private final List<TaxonomyElement> parents;
        private final List<TaxonomyElement> children;
        private double popularity;

        private TaxonomyElement(Element element)
        {
            this.element = element;
            this.parents = new ArrayList<TaxonomyElement>();
            this.children = new ArrayList<TaxonomyElement>();
            popularity = element instanceof Item ? element.getPopularity() : 0;
        }

        private void addParent(TaxonomyElement parent)
        {
            parents.add(parent);
        }

        private void addChild(TaxonomyElement element)
        {
            children.add(element);
            if (element.element instanceof Item)
            {
                popularity += element.popularity;
            }
        }

        public int compareTo(Object o)
        {
            if (!(o instanceof TaxonomyElement)) throw new IllegalArgumentException("Invalid comparison of taxonomy element");
            return Double.compare(((TaxonomyElement) o).popularity, popularity);
        }

        @Override
        public String toString()
        {
            return String.format("[%.3f] %s. ", popularity, element.getName()) +
                    elementNames("PARENTS", parents) + elementNames("CHILDREN", children);
        }

        private String elementNames(String title, List<TaxonomyElement> elements)
        {
            if (elements.isEmpty()) return "";
            String result = title + ": ";
            for (TaxonomyElement taxonomyElement : elements)
            {
                result += taxonomyElement.element.getName() + ", ";
            }
            return result;
        }
    }
}
