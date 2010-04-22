package ru.lavila.menudesigner.models;

import ru.lavila.menudesigner.models.events.ElementChangeEvent;
import ru.lavila.menudesigner.models.events.ElementListener;
import ru.lavila.menudesigner.models.events.StructureChangeEvent;
import ru.lavila.menudesigner.models.events.StructureChangeEventImpl;

import java.util.*;

public class CategoryImpl extends ElementImpl implements Category, ElementListener
{
    private final List<Element> elements;

    public CategoryImpl(String name)
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
        fireStructureChanged(StructureChangeEvent.EventType.ELEMENTS_ADDED, newElements);
    }

    public void remove(Element... elementsToRemove)
    {
        List<Element> elements = Arrays.asList(elementsToRemove);
        this.elements.removeAll(elements);
        fireStructureChanged(StructureChangeEvent.EventType.ELEMENTS_REMOVED, elementsToRemove);
    }

    private void fireStructureChanged(StructureChangeEvent.EventType type, Element... elements)
    {
        StructureChangeEvent event = new StructureChangeEventImpl(type, this, elements);
        for (ElementListener listener : listeners)
        {
            listener.structureChanged(event);
        }
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

    public void structureChanged(StructureChangeEvent event)
    {
    }
}
