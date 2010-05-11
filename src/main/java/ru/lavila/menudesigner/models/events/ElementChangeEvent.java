package ru.lavila.menudesigner.models.events;

import ru.lavila.menudesigner.models.Element;

public interface ElementChangeEvent
{
    public Element getElement();
    public EventType getType();

    enum EventType
    {
        NAME_CHANGED,
        POPULARITY_CHANGED
    }
}
