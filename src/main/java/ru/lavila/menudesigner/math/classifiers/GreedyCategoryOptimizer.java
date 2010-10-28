package ru.lavila.menudesigner.math.classifiers;

import ru.lavila.menudesigner.models.Category;
import ru.lavila.menudesigner.models.Element;
import ru.lavila.menudesigner.models.Hierarchy;
import ru.lavila.menudesigner.models.Item;
import ru.lavila.menudesigner.models.menumodels.MenuModel;

import java.util.ArrayList;
import java.util.List;

public class GreedyCategoryOptimizer extends AbstractClassifier
{
    public GreedyCategoryOptimizer(Hierarchy targetHierarchy, Category category)
    {
        super(targetHierarchy, category);
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
}
