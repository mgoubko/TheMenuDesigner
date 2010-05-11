package ru.lavila.menudesigner.controllers;

import ru.lavila.menudesigner.models.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DesignerController
{
    private final Hierarchy targetHierarchy;

    public DesignerController(Hierarchy targetHierarchy)
    {
        this.targetHierarchy = targetHierarchy;
    }

    public void aliasElements(Category targetCategory, List<Element> sourceElements)
    {
        Map<Category, List<Element>> categories = new HashMap<Category, List<Element>>();
        for (Element element : sourceElements)
        {
            if (element instanceof Item)
            {
                targetHierarchy.add(targetCategory, element);
            }
            else if (element instanceof Category)
            {
                Category category = targetHierarchy.newCategory(targetCategory, element.getName());
                categories.put(category, ((Category) element).getElements());
            }
        }
        for (Category category : categories.keySet())
        {
            aliasElements(category, categories.get(category));
        }
    }
}
