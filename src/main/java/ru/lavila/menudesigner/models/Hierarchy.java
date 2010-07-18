package ru.lavila.menudesigner.models;

import ru.lavila.menudesigner.models.events.HierarchyListener;

import java.util.List;

public interface Hierarchy
{
    public final String LEVEL_SEPARATOR = ">";

    String getName();
    public Category getRoot();
    public boolean isTaxomony();
    public List<Category> getAllCategories();
    public Category getElementCategory(Element element);
    public Item newItem(Category parentCategory, String name, double popularity);
    public Category newCategory(Category parentCategory, String name);
    public void add(Category category, Element... elements);
    public void add(Category category, int index, Element... elements);
    public void remove(Element... elements);
    public String getPathTo(Element element);

    public void addModelListener(HierarchyListener listener);
    public void removeModelListener(HierarchyListener listener);
}
