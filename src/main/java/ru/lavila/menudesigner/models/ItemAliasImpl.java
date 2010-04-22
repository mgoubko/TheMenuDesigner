package ru.lavila.menudesigner.models;

public class ItemAliasImpl implements Item
{
    public final Item sourceItem;
    private String name;

    public ItemAliasImpl(Item sourceItem)
    {
        this(null, sourceItem);
    }

    public ItemAliasImpl(String name, Item sourceItem)
    {
        if (sourceItem == null) throw new IllegalArgumentException("Alias source can't be null");
        this.sourceItem = sourceItem;
        this.name = name;
    }

    public void setPopularity(double popularity)
    {
        sourceItem.setPopularity(popularity);
    }

    public String getName()
    {
        return name != null ? name : sourceItem.getName();
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public double getPopularity()
    {
        return sourceItem.getPopularity();
    }
}
