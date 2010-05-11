package ru.lavila.menudesigner.models.impl;

import ru.lavila.menudesigner.models.Element;
import ru.lavila.menudesigner.models.events.ElementChangeEvent;
import ru.lavila.menudesigner.models.events.ElementListener;

import java.util.ArrayList;
import java.util.List;

abstract class ElementImpl implements Element
{
    protected String name;
    protected final List<ElementListener> listeners;

    protected ElementImpl(String name)
    {
        this.name = name;
        listeners = new ArrayList<ElementListener>();
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
        fireNameChanged();
    }

    public void addModelListener(ElementListener listener)
    {
        listeners.add(listener);
    }

    public void removeModelListener(ElementListener listener)
    {
        listeners.remove(listener);
    }

    protected void fireModelEvent(ElementChangeEvent event)
    {
        for (ElementListener listener : listeners)
        {
            listener.modelChanged(event);
        }
    }

    protected void fireNameChanged()
    {
        fireModelEvent(new ElementChangeEventImpl(this, ElementChangeEvent.EventType.NAME_CHANGED));
    }

    protected void firePopularityChanged()
    {
        fireModelEvent(new ElementChangeEventImpl(this, ElementChangeEvent.EventType.POPULARITY_CHANGED));
    }

    private static class ElementChangeEventImpl implements ElementChangeEvent
    {
        private final Element element;
        private final EventType type;

        public ElementChangeEventImpl(Element element, EventType type)
        {
            this.element = element;
            this.type = type;
        }

        public Element getElement()
        {
            return element;
        }

        public EventType getType()
        {
            return type;
        }
    }
}
