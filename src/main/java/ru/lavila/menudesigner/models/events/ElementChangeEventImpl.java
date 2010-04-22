package ru.lavila.menudesigner.models.events;

import ru.lavila.menudesigner.models.Element;

public class ElementChangeEventImpl<ValueType> implements ElementChangeEvent
{
    private final Element element;
    private final EventType type;
    private final ValueType oldValue;
    private final ValueType newValue;

    public ElementChangeEventImpl(Element element, EventType type, ValueType oldValue, ValueType newValue)
    {
        this.element = element;
        this.type = type;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public Element getElement()
    {
        return element;
    }

    public EventType getType()
    {
        return type;
    }

    public Object getOldValue()
    {
        return oldValue;
    }

    public Object getNewValue()
    {
        return newValue;
    }
}
