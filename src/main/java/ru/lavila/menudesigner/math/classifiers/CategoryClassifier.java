package ru.lavila.menudesigner.math.classifiers;

import ru.lavila.menudesigner.models.Category;
import ru.lavila.menudesigner.models.Element;
import ru.lavila.menudesigner.models.Hierarchy;

import java.util.*;

public class CategoryClassifier extends AbstractTaxonomyElementsClassifier
{
    private final CategoryManipulator manipulator;

    public CategoryClassifier(CategoryManipulator manipulator)
    {
        this.manipulator = manipulator;
    }

    public void classify(Hierarchy taxonomy)
    {
        List<TaxonomyElement> taxonomyElements = collectTaxonomyElements(taxonomy.getRoot(), manipulator.category.getGroup(), 0);

        manipulator.cleanup();

        Map<Category, Category> categories = new HashMap<Category, Category>();
        for (TaxonomyElement taxonomyElement : taxonomyElements)
        {
            Element element = taxonomyElement.element;
            Category parentCategory = manipulator.category;
            if (!taxonomyElement.parents.isEmpty())
            {
                parentCategory = categories.get(taxonomyElement.parents.get(0).element);
            }
            if (element instanceof Category)
            {
                Category newCategory = manipulator.hierarchy.newCategory(parentCategory, element.getName());
                categories.put((Category) element, newCategory);
            }
            else
            {
                manipulator.hierarchy.add(parentCategory, element);
            }
        }
    }
}
