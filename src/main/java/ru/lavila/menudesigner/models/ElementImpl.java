package ru.lavila.menudesigner.models;

import java.util.ArrayList;
import java.util.List;

public abstract class ElementImpl implements Element
{
    protected final List<ElementListener> listeners;

    public ElementImpl()
    {
        this.listeners = new ArrayList<ElementListener>();
    }

    public void addModelListener(ElementListener listener)
    {
        listeners.add(listener);
    }
}
