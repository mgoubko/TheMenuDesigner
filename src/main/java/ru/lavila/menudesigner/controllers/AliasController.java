package ru.lavila.menudesigner.controllers;

import ru.lavila.menudesigner.models.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AliasController
{
    private final Hierarchy sourceHierarchy;
    private final Hierarchy targetHierarchy;

    public AliasController(Hierarchy sourceHierarchy, Hierarchy targetHierarchy)
    {
        this.sourceHierarchy = sourceHierarchy;
        this.targetHierarchy = targetHierarchy;
    }

    public void aliasElements(Category targetCategory, List<Element> sourceElements)
    {
        List<Element> elements = new ArrayList<Element>();
        Map<Category, List<Element>> categories = new HashMap<Category, List<Element>>();
        for (Element element : sourceElements)
        {
            if (element instanceof Item)
            {
                elements.add(new ItemAliasImpl((Item) element));
            }
            else if (element instanceof Category)
            {
                Category category = new CategoryImpl(element.getName());
                elements.add(category);
                categories.put(category, ((Category) element).getElements());
            }
        }
        targetCategory.add(elements.toArray(new Element[elements.size()]));
        for (Category category : categories.keySet())
        {
            aliasElements(category, categories.get(category));
        }
    }
}
