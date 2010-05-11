package ru.lavila.menudesigner.models.events;

public interface ItemsListListener
{
    public void itemChanged(ElementChangeEvent event);
    public void listChanged(ItemsListChangeEvent event);
}
