package ru.lavila.menudesigner.math.classifiers;

import ru.lavila.menudesigner.models.Category;
import ru.lavila.menudesigner.models.Element;
import ru.lavila.menudesigner.models.Hierarchy;
import ru.lavila.menudesigner.models.Item;
import ru.lavila.menudesigner.models.menumodels.MenuModel;

import java.util.ArrayList;
import java.util.List;

public class GreedyCategoryOptimizer extends AbstractTaxonomyElementsClassifier
{
    private final CategoryManipulator manipulator;
    private final Category category;
    private final Hierarchy hierarchy;
    private final MenuModel menuModel;
    private List<TaxonomyElement> taxonomyElements;
    private List<Element> split;

    public GreedyCategoryOptimizer(CategoryManipulator manipulator, MenuModel menuModel)
    {
        this.manipulator = manipulator;
        this.category = manipulator.category;
        this.hierarchy = manipulator.hierarchy;
        this.menuModel = menuModel;
    }

    public Split optimize(Hierarchy taxonomy)
    {
        double[] proportion = menuModel.getOptimalProportion();
        double groupPopularity = 0;
        this.taxonomyElements = collectTaxonomyElements(taxonomy.getRoot(), category.getGroup(), proportion[proportion.length - 1]);
        this.split = new ArrayList<Element>();

        for (Item item : category.getGroup())
        {
            groupPopularity += item.getPopularity();
        }

        manipulator.cleanup();

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
                Category newCategory = hierarchy.newCategory(category, "...");
                split.add(newCategory);
                for (TaxonomyElement taxonomyElement : taxonomyElements)
                {
                    if (taxonomyElement.element instanceof Item)
                    {
                        hierarchy.add(newCategory, taxonomyElement.element);
                    }
                }
            }
        }

        return new Split(split);
    }

    private void addElementToSplit(TaxonomyElement taxonomyElement)
    {
        taxonomyElements.remove(taxonomyElement);
        taxonomyElements.removeAll(taxonomyElement.parents);
        taxonomyElements.removeAll(taxonomyElement.children);
        if (taxonomyElement.element instanceof Category)
        {
            Category newCategory = hierarchy.newCategory(category, taxonomyElement.element.getName());
            split.add(newCategory);
            List<Item> items = new ArrayList<Item>();
            for (TaxonomyElement child : taxonomyElement.children)
            {
                if (child.element instanceof Item)
                {
                    items.add((Item) child.element);
                }
            }
            hierarchy.add(newCategory, items.toArray(new Item[items.size()]));
        }
        else
        {
            hierarchy.add(category, taxonomyElement.element);
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
