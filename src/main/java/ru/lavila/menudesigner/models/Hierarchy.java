package ru.lavila.menudesigner.models;

import java.util.ArrayList;
import java.util.List;

public class Hierarchy
{
    public final Category root;

    public Hierarchy()
    {
        root = new RootCategoryImpl();
    }

    public List<Item> getItems()
    {
        return getItems(root);
    }

    private List<Item> getItems(Category category)
    {
        List<Item> items = new ArrayList<Item>();
        for (Element element : category.getElements())
        {
            if (element instanceof Item)
            {
                items.add((Item) element);
            }
            else if (element instanceof Category)
            {
                items.addAll(getItems((Category) element));
            }
        }
        return items;
    }
}
