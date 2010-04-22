package ru.lavila.menudesigner.models.events;

public interface HierarchyListener
{
    public void modelChanged(ElementChangeEvent event);
    public void structureChanged(StructureChangeEvent event);
}
