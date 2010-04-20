package ru.lavila.menudesigner.models;

public class ItemImpl extends ElementImpl implements Item
{
    private String name;
    private double popularity;

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

    public void setName(String name)
    {
        this.name = name;
    }

    public void setPopularity(double popularity)
    {
        this.popularity = popularity;
    }
}
