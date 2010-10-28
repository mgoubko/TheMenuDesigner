package ru.lavila.menudesigner.math.classifiers;

import ru.lavila.menudesigner.models.Category;
import ru.lavila.menudesigner.models.Element;
import ru.lavila.menudesigner.models.Hierarchy;
import ru.lavila.menudesigner.models.Item;

import java.util.*;

public abstract class AbstractClassifier
{
    protected final Hierarchy targetHierarchy;
    protected final Category category;
    protected List<TaxonomyElement> taxonomyElements;
    protected List<Element> split;
    protected double groupPopularity = 0;

    public AbstractClassifier(Hierarchy targetHierarchy, Category category)
    {
        this.targetHierarchy = targetHierarchy;
        this.category = category;
    }

    protected void cleanupCategory()
    {
        List<Element> children = category.getElements();
        targetHierarchy.remove(children.toArray(new Element[children.size()]));
    }

    protected void applySplitToCategory()
    {
        targetHierarchy.add(category, split.toArray(new Element[split.size()]));
    }

    protected List<TaxonomyElement> collectTaxonomyElements(Category taxonomyCategory, List<Item> targetItems, double ignoreLimit)
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
