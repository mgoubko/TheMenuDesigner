package ru.lavila.menudesigner.models;

import java.util.*;

public class CategoryImpl extends ElementImpl implements Category
{
    private String name;
    private final List<Element> elements;

    public CategoryImpl(String name)
    {
        this.name = name;
        elements = new ArrayList<Element>();
    }

    public List<Element> getElements()
    {
        return Collections.unmodifiableList(elements);
    }

    public void add(Element... newElements)
    {
        elements.addAll(Arrays.asList(newElements));
        fireElementsAdded(this, newElements);
    }

    public String getName()
    {
        return name;
    }

    public double getPopularity()
    {
        double popularity = 0;
        for (Element element : elements)
        {
            popularity += element.getPopularity();
        }
        return popularity;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
