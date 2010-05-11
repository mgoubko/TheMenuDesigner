package ru.lavila.menudesigner.models.events;

import ru.lavila.menudesigner.models.Item;

import java.util.List;

public interface ItemsListChangeEvent
{
    public EventType getType();
    public List<Item> getItems();
    public List<Integer> getIndexes();

    enum EventType
    {
        ELEMENTS_ADDED,
        ELEMENTS_REMOVED
    }
}
