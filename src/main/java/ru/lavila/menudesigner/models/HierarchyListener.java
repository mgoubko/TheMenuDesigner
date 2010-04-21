package ru.lavila.menudesigner.models;

import java.util.Collection;
import java.util.Map;

public interface HierarchyListener
{
    void elementsAdded(Category parent, Element... elements);
    void elementsRemoved(Map<Category, Collection<Element>> elementsMap);
}
