package ru.lavila.menudesigner.models.events;

import ru.lavila.menudesigner.models.CategorizedElements;

public interface StructureChangeEvent
{
    public EventType getType();
    public CategorizedElements getCategorizedElements();

    enum EventType
    {
        ELEMENTS_ADDED,
        ELEMENTS_REMOVED
    }
}
