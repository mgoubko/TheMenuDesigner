package ru.lavila.menudesigner.controllers;

import ru.lavila.menudesigner.models.*;

import java.util.ArrayList;
import java.util.List;

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
        List<Item> items = new ArrayList<Item>();
        for (Element element : sourceElements)
        {
            if (element instanceof Item)
            {
                items.add(new ItemAliasImpl((Item) element));
            }
        }
        targetCategory.add(items.toArray(new Item[items.size()]));
    }
}
