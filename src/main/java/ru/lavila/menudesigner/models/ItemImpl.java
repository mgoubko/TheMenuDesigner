package ru.lavila.menudesigner.models;

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
        double oldPopularity = this.popularity;
        this.popularity = popularity;
        firePopularityChanged(oldPopularity, popularity);
    }
}
