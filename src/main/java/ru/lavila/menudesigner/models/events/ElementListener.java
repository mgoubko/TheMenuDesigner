package ru.lavila.menudesigner.models.events;

public interface ElementListener
{
    public void modelChanged(ElementChangeEvent event);
    public void structureChanged(StructureChangeEvent event);
}
