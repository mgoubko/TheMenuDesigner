package ru.lavila.menudesigner.models;

public interface ElementListener
{
    void elementsAdded(Category parent, Element... elements);
    void elementsRemoved(Category parent, Element... elements);
}
