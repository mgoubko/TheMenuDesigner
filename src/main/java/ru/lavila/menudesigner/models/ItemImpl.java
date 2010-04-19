package ru.lavila.menudesigner.models;

public class ItemImpl implements Item
{
    private final String name;
    private final double popularity;

    public ItemImpl(String name, double popularity)
    {
        this.name = name;
        this.popularity = popularity;
    }

    public String getName()
    {
        return name;
    }

    public double getPopularity()
    {
        return popularity;
    }
}
