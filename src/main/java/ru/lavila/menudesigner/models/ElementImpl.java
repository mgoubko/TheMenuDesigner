package ru.lavila.menudesigner.models;

import ru.lavila.menudesigner.models.events.ElementChangeEvent;
import ru.lavila.menudesigner.models.events.ElementChangeEventImpl;
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
        String oldName = this.name;
        this.name = name;
        fireNameChanged(oldName, name);
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

    protected void fireNameChanged(String oldValue, String newValue)
    {
        fireModelEvent(new ElementChangeEventImpl<String>(this, ElementChangeEvent.EventType.NAME_CHANGED, oldValue, newValue));
    }

    protected void firePopularityChanged(double oldValue, double newValue)
    {
        fireModelEvent(new ElementChangeEventImpl<Double>(this, ElementChangeEvent.EventType.POPULARITY_CHANGED, oldValue, newValue));
    }
}
