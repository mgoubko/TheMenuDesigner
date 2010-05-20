package ru.lavila.menudesigner.controllers;

import ru.lavila.menudesigner.models.*;

public class ItemsController
{
    private final ItemsList itemsList;

    public ItemsController(ItemsList itemsList)
    {
        this.itemsList = itemsList;
    }

    public void normalizePopularities()
    {
        itemsList.normalizePopularities();
    }
}
