package ru.lavila.menudesigner.models;

import ru.lavila.menudesigner.models.events.HierarchyListener;

public interface Hierarchy
{
    public Category getRoot();
    public boolean isTaxomony();
    public Item newItem(Category parentCategory, String name, double popularity);
    public Category newCategory(Category parentCategory, String name);
    public void add(Category category, Element... elements);
    public void remove(Element... elements);

    public void addModelListener(HierarchyListener listener);
    public void removeModelListener(HierarchyListener listener);
}
