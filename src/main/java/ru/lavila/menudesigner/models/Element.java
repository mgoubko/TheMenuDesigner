package ru.lavila.menudesigner.models;

public interface Element
{
    public String getName();
    public void setName(String name);
    public double getPopularity();
    public void addModelListener(ElementListener listener);
}
