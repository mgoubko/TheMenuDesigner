package ru.lavila.menudesigner.models.events;

import ru.lavila.menudesigner.models.CategorizedElements;
import ru.lavila.menudesigner.models.Hierarchy;

public interface StructureChangeEvent
{
    public Hierarchy getSource();
    public CategorizedElements getElementsAdded();
    public CategorizedElements getElementsRemoved();
}
