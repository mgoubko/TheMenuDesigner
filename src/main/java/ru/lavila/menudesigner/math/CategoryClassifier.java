package ru.lavila.menudesigner.math;

import ru.lavila.menudesigner.models.Category;
import ru.lavila.menudesigner.models.Element;
import ru.lavila.menudesigner.models.Hierarchy;
import ru.lavila.menudesigner.models.Item;
import ru.lavila.menudesigner.models.menumodels.MenuModel;

import java.util.*;

public class CategoryClassifier
{
    private final Hierarchy targetHierarchy;
    private final Category category;
    private List<TaxonomyElement> taxonomyElements;
    private List<Element> split;
    private double groupPopularity = 0;

    public CategoryClassifier(Hierarchy targetHierarchy, Category category)
    {
        this.targetHierarchy = targetHierarchy;
        this.category = category;
    }

    public void classify(Hierarchy taxonomy)
    {
        this.taxonomyElements = collectTaxonomyElements(taxonomy.getRoot(), category.getGroup(), 0);
        this.split = new ArrayList<Element>();

        cleanupCategory();

        Map<Category, Category> categories = new HashMap<Category, Category>();
        for (TaxonomyElement taxonomyElement : taxonomyElements)
        {
            Element element = taxonomyElement.element;
            Category parentCategory = category;
            if (!taxonomyElement.parents.isEmpty())
            {
                parentCategory = categories.get(taxonomyElement.parents.get(0).element);
            }
            if (element instanceof Category)
            {
                Category newCategory = targetHierarchy.newCategory(parentCategory, element.getName());
                categories.put((Category) element, newCategory);
                if (parentCategory == category) split.add(newCategory);
            }
            else
            {
                targetHierarchy.add(parentCategory, element);
                if (parentCategory == category) split.add(element);
            }
        }

        applySplitToCategory();
    }

    public void optimize(Hierarchy taxonomy, MenuModel menuModel)
    {
        double[] proportion = menuModel.getOptimalProportion();
        this.taxonomyElements = collectTaxonomyElements(taxonomy.getRoot(), category.getGroup(), proportion[proportion.length - 1]);
        this.split = new ArrayList<Element>();

        for (Item item : category.getGroup())
        {
            groupPopularity += item.getPopularity();
        }

        cleanupCategory();

        // greedy algorithm to fill category close to optimal proportion
        for (int index = 0; index < proportion.length - 1; index++)
        {
            if (taxonomyElements.isEmpty()) break;
            addElementToSplit(findClosestTaxonomyElement(groupPopularity * proportion[index]));
        }

        // increase branching factor to make '...' category the smallest one
        while (!taxonomyElements.isEmpty() && taxonomyElementsPopularity() > split.get(split.size() - 1).getPopularity())
        {
            addElementToSplit(taxonomyElements.get(0));
        }

        if (!taxonomyElements.isEmpty())
        {
            // if taxonomyElements contains single element or single category
            if (taxonomyElements.get(0).children.size() + 1 == taxonomyElements.size())
            {
                addElementToSplit(taxonomyElements.get(0));
            }
            else
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
        }
        applySplitToCategory();
    }

    public void flatten()
    {
        List<Item> items = category.getGroup();
        cleanupCategory();
        targetHierarchy.add(category, items.toArray(new Element[items.size()]));
    }

    private void cleanupCategory()
    {
        List<Element> children = category.getElements();
        targetHierarchy.remove(children.toArray(new Element[children.size()]));
    }

    private void applySplitToCategory()
    {
        targetHierarchy.add(category, split.toArray(new Element[split.size()]));
    }

    private void addElementToSplit(TaxonomyElement taxonomyElement)
    {
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

    private TaxonomyElement findClosestTaxonomyElement(double value)
    {
        double lastDiff = -1;
        TaxonomyElement lastElement = null;
        for (TaxonomyElement taxonomyElement : taxonomyElements)
        {
            double diff = taxonomyElement.popularity - value;
            if (diff > 0)
            {
                if (taxonomyElement.terminal) return taxonomyElement;
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

    private double taxonomyElementsPopularity()
    {
        double sum = 0;
        for (TaxonomyElement taxonomyElement : taxonomyElements)
        {
            sum += taxonomyElement.popularity;
        }
        return sum;
    }

    private List<TaxonomyElement> collectTaxonomyElements(Category taxonomyCategory, List<Item> targetItems, double ignoreLimit)
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
                List<TaxonomyElement> childTaxonomyElements = collectTaxonomyElements(category, targetItems, ignoreLimit);
                if (!childTaxonomyElements.isEmpty())
                {
                    TaxonomyElement categoryTaxonomyElement = new TaxonomyElement(category);
                    taxonomyElements.add(categoryTaxonomyElement);
                    for (TaxonomyElement taxonomyElement : childTaxonomyElements)
                    {
                        categoryTaxonomyElement.addChild(taxonomyElement);
                        if (taxonomyElement.popularity >= ignoreLimit)
                        {
                            categoryTaxonomyElement.terminal = false;
                        }
                        taxonomyElement.addParent(categoryTaxonomyElement);
                        taxonomyElements.add(taxonomyElement);
                    }
                }
            }
        }
        Collections.sort(taxonomyElements);
        return taxonomyElements;
    }
}
