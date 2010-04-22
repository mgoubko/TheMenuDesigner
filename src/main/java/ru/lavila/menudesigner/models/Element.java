package ru.lavila.menudesigner.models;

import ru.lavila.menudesigner.models.events.ElementListener;

public interface Element
{
    public String getName();
    public void setName(String name);
    public double getPopularity();
    public void addModelListener(ElementListener listener);
    public void removeModelListener(ElementListener listener);
}
