package ru.lavila.menudesigner.math;

import ru.lavila.menudesigner.models.Category;
import ru.lavila.menudesigner.models.Element;
import ru.lavila.menudesigner.models.Hierarchy;

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
        List<Element> elements = category.getElements();
        targetHierarchy.add(targetHierarchy.newCategory(category, "Classified"), elements.toArray(new Element[elements.size()]));
    }
}
