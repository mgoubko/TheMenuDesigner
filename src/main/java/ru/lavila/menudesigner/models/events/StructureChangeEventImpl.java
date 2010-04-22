package ru.lavila.menudesigner.models.events;

import ru.lavila.menudesigner.models.CategorizedElements;
import ru.lavila.menudesigner.models.Category;
import ru.lavila.menudesigner.models.Element;

import java.util.List;

public class StructureChangeEventImpl implements StructureChangeEvent
{
    private final EventType type;
    private final CategorizedElements elements;

    public StructureChangeEventImpl(EventType type, CategorizedElements elements)
    {
        this.type = type;
        this.elements = elements;
    }

    public StructureChangeEventImpl(EventType type, Category category, Element... children)
    {
        this.type = type;
        elements = new CategorizedElements();
        elements.add(category, children);
    }

    public EventType getType()
    {
        return type;
    }

    public CategorizedElements getCategorizedElements()
    {
        return elements;
    }
}
