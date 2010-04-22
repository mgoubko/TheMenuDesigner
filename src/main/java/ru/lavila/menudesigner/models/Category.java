package ru.lavila.menudesigner.models;

import java.util.List;

public interface Category extends Element
{
    public List<Element> getElements();
    public int elementsCount();
    public void add(Element... elements);
    public void remove(Element... elements);
}
