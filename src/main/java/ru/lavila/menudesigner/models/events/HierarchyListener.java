package ru.lavila.menudesigner.models.events;

public interface HierarchyListener
{
    public void elementChanged(ElementChangeEvent event);
    public void structureChanged(StructureChangeEvent event);
}
