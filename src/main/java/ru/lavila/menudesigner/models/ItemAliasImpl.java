package ru.lavila.menudesigner.models;

import ru.lavila.menudesigner.models.events.ElementChangeEvent;
import ru.lavila.menudesigner.models.events.ElementListener;
import ru.lavila.menudesigner.models.events.StructureChangeEvent;

public class ItemAliasImpl extends ElementImpl implements Item, ElementListener
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
        sourceItem.addModelListener(this);
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

    public void modelChanged(ElementChangeEvent event)
    {
        switch (event.getType())
        {
            case NAME_CHANGED:
                if (name == null) fireNameChanged((String) event.getOldValue(), (String) event.getNewValue());
                break;
            case POPULARITY_CHANGED:
                firePopularityChanged((Double) event.getOldValue(), (Double) event.getNewValue());
                break;
        }
    }

    public void structureChanged(StructureChangeEvent event)
    {
    }
}
