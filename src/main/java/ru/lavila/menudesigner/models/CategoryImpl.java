package ru.lavila.menudesigner.models;

import ru.lavila.menudesigner.models.events.ElementChangeEvent;
import ru.lavila.menudesigner.models.events.StructureChangeEventImpl;

import java.util.*;

public class CategoryImpl extends ElementImpl implements Category
{
    private final List<Element> elements;

    //todo: listen to popularity change and forward event
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
        fireStructureChanged(ElementChangeEvent.EventType.ELEMENTS_ADDED, elements);
    }

    public void remove(Element... elementsToRemove)
    {
        List<Element> elements = Arrays.asList(elementsToRemove);
        this.elements.removeAll(elements);
        fireStructureChanged(ElementChangeEvent.EventType.ELEMENTS_REMOVED, elements);
    }

    private void fireStructureChanged(ElementChangeEvent.EventType type, List<Element> diff)
    {
        fireModelEvent(new StructureChangeEventImpl(this, type, diff));
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
