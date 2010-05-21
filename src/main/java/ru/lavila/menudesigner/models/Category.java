package ru.lavila.menudesigner.models;

import java.util.List;

public interface Category extends Element
{
    public List<Element> getElements();
    public boolean isEmpty();

    List<Item> getGroup();
}
