package ru.lavila.menudesigner.models;

import java.util.*;

public class CategoryImpl implements Category
{
    private String name;
    private final List<Element> elements;
    private final List<CategoryListener> listeners;

    public CategoryImpl(String name)
    {
        this.name = name;
        elements = new ArrayList<Element>();
        listeners = new ArrayList<CategoryListener>();
    }

    public List<Element> getElements()
    {
        return Collections.unmodifiableList(elements);
    }

    public int elementsCount()
    {
        return elements.size();
    }

    public void add(Element... newElements)
    {
        elements.addAll(Arrays.asList(newElements));
        fireElementsAdded(newElements);
    }

    public void remove(Element... elementsToRemove)
    {
        elements.removeAll(Arrays.asList(elementsToRemove));
        fireElementsRemoved(elementsToRemove);
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

    public void addModelListener(CategoryListener listener)
    {
        listeners.add(listener);
    }

    public void removeModelListener(CategoryListener listener)
    {
        listeners.remove(listener);
    }

    protected void fireElementsAdded(Element... elements)
    {
        for (CategoryListener listener : listeners)
        {
            listener.elementsAdded(this, elements);
        }
    }

    protected void fireElementsRemoved(Element... elements)
    {
        for (CategoryListener listener : listeners)
        {
            listener.elementsRemoved(this, elements);
        }
    }
}
