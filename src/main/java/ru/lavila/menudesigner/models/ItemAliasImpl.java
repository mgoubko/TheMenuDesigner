package ru.lavila.menudesigner.models;

public class ItemAliasImpl extends ElementImpl implements Item
{
    public final Item sourceItem;

    public ItemAliasImpl(Item sourceItem)
    {
        this(null, sourceItem);
    }

    public ItemAliasImpl(String name, Item sourceItem)
    {
        super(name);
        if (sourceItem == null) throw new IllegalArgumentException("Alias source can't be null");
        this.sourceItem = sourceItem;
    }

    public void setPopularity(double popularity)
    {
        sourceItem.setPopularity(popularity);
    }

    public String getName()
    {
        return name != null ? name : sourceItem.getName();
    }

    public double getPopularity()
    {
        return sourceItem.getPopularity();
    }
}
