package ru.lavila.menudesigner.models;

import ru.lavila.menudesigner.models.events.ElementChangeEvent;
import ru.lavila.menudesigner.models.events.ElementListener;

import java.util.*;

class CategoryImpl extends ElementImpl implements Category, ElementListener
{
    private final List<Element> elements;

    CategoryImpl(String name)
    {
        super(name);
        elements = new ArrayList<Element>();
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
        List<Element> elements = Arrays.asList(newElements);
        this.elements.addAll(elements);
        for (Element element : elements)
        {
            element.addModelListener(this);
        }
        firePopularityChanged(-1, getPopularity());
    }

    public void remove(Element... elementsToRemove)
    {
        List<Element> elements = Arrays.asList(elementsToRemove);
        this.elements.removeAll(elements);
        firePopularityChanged(-1, getPopularity());
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

    public void modelChanged(ElementChangeEvent event)
    {
        // child popularity changed, so my popularity changes too
        if (event.getType() == ElementChangeEvent.EventType.POPULARITY_CHANGED)
        {
            // todo: cache category popularity to be able to send old value and for performance issues
            firePopularityChanged(-1, getPopularity());
        }
    }
}
