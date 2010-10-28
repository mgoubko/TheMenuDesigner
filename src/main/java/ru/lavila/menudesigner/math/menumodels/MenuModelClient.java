package ru.lavila.menudesigner.math.menumodels;

import ru.lavila.menudesigner.models.menumodels.MenuModelListener;

import java.util.ArrayList;
import java.util.List;

public abstract class MenuModelClient
{
    private final List<MenuModelListener> listeners = new ArrayList<MenuModelListener>();

    public void addModelListener(MenuModelListener listener)
    {
        if (!listeners.contains(listener))
        {
            listeners.add(listener);
        }
    }

    public void removeModelListener(MenuModelListener listener)
    {
        listeners.remove(listener);
    }

    protected void fireModelChanged()
    {
        for (MenuModelListener listener : listeners)
        {
            listener.menuModelChanged();
        }
    }
}
