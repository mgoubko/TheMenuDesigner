package ru.lavila.menudesigner.models.impl;

import ru.lavila.menudesigner.models.Item;

class ItemImpl extends ElementImpl implements Item
{
    private double popularity;

    ItemImpl(String name, double popularity)
    {
        super(name);
        this.popularity = popularity;
    }

    public double getPopularity()
    {
        return popularity;
    }

    public void setPopularity(double popularity)
    {
        this.popularity = popularity;
        firePopularityChanged();
    }
}
