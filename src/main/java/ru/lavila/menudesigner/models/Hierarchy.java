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
        for (Choice choice : category.getChoices())
        {
            if (choice instanceof Item)
            {
                items.add((Item) choice);
            }
            else if (choice instanceof Category)
            {
                items.addAll(getItems((Category) choice));
            }
        }
        return items;
    }
}
