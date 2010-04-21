package ru.lavila.menudesigner.models;

import java.util.List;

public interface Category extends Element
{
    public List<Element> getElements();
    public int elementsCount();
    public void add(Element... elements);
    public void remove(Element... elements);
    void addModelListener(CategoryListener listener);
    void removeModelListener(CategoryListener listener);

    interface CategoryListener
    {
        void elementsAdded(Category category, Element... elements);
        void elementsRemoved(Category category, Element... elements);
    }
}
