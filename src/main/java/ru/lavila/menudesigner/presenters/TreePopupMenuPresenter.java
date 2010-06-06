package ru.lavila.menudesigner.presenters;

import ru.lavila.menudesigner.models.Hierarchy;
import ru.lavila.menudesigner.models.ItemsList;

import java.util.List;

public class TreePopupMenuPresenter
{
    private final ItemsList itemsList;

    public TreePopupMenuPresenter(ItemsList itemsList)
    {
        this.itemsList = itemsList;
    }

    public List<Hierarchy> getTaxonomies()
    {
        return itemsList.getTaxonomies();
    }
}
