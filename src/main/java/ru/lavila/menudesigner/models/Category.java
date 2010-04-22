package ru.lavila.menudesigner.models;

import java.util.List;

public interface Category extends Element
{
    public List<Element> getElements();
    public int elementsCount();

    //todo: handle all structure changes via hierarchy without access to categories per se
    public void add(Element... elements);
    public void remove(Element... elements);
}
