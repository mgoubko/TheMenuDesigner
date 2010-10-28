package ru.lavila.menudesigner.math.classifiers;

import ru.lavila.menudesigner.models.Category;
import ru.lavila.menudesigner.models.Element;
import ru.lavila.menudesigner.models.Hierarchy;
import ru.lavila.menudesigner.models.Item;

import java.util.*;

public class CategoryClassifier extends AbstractClassifier
{
    public CategoryClassifier(Hierarchy targetHierarchy, Category category)
    {
        super(targetHierarchy, category);
    }

    public void flatten()
    {
        List<Item> items = category.getGroup();
        cleanupCategory();
        targetHierarchy.add(category, items.toArray(new Element[items.size()]));
    }

    public void sortByPopularity()
    {
        List<Element> elements = new ArrayList<Element>(category.getElements());
        Collections.sort(elements, new Comparator<Element>()
        {
            public int compare(Element element1, Element element2)
            {
                return Double.compare(element2.getPopularity(), element1.getPopularity());
            }
        });
        targetHierarchy.add(category, elements.toArray(new Element[elements.size()]));
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
}
