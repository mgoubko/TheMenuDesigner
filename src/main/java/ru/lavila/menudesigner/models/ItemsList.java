package ru.lavila.menudesigner.models;

import ru.lavila.menudesigner.models.events.ItemsListListener;

import java.util.List;

public interface ItemsList
{
    public Item[] toArray();
    public Item get(int index);
    public int indexOf(Item item);
    public int size();
    public Item newItem(String name, double popularity);
    public void remove(Item... items);

    public List<Hierarchy> getHierarchies();
    public List<Hierarchy> getTaxonomies();
    public Hierarchy newHierarchy(String name, boolean taxomony);

    public void addModelListener(ItemsListListener listener);
    public void removeModelListener(ItemsListListener listener);
}
