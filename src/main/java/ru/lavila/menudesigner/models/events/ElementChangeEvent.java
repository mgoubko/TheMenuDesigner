package ru.lavila.menudesigner.models.events;

import ru.lavila.menudesigner.models.Element;

public interface ElementChangeEvent<ValueType>
{
    public Element getElement();
    public EventType getType();
    public ValueType getOldValue();
    public ValueType getNewValue();

    enum EventType
    {
        NAME_CHANGED,
        POPULARITY_CHANGED
    }
}
